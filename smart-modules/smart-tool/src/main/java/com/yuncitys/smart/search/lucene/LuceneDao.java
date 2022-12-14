package com.yuncitys.smart.search.lucene;

import com.yuncitys.smart.search.lucene.util.DocumentUtil;
import com.yuncitys.smart.search.lucene.util.IKAnalyzer5x;
import com.yuncitys.smart.search.lucene.util.QueryUtil;
import com.yuncitys.smart.parking.api.vo.search.IndexObject;
import com.yuncitys.smart.parking.common.msg.TableResultResponse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:lucene
 *
 * @author smart
 * @create 2017-05-18
 **/
public class LuceneDao {

	private final static Logger log = LoggerFactory.getLogger(LuceneDao.class);

	private  Directory directory = null;
	private  Analyzer analyzer = null;
	private String indexDer = null;

	public void setIndexDer(String indexDer) {
		this.indexDer = indexDer;
	}

	public Analyzer getAnalyzer() {
		if(analyzer == null){
	         analyzer = new IKAnalyzer5x(true);
		}
		return analyzer;
	}

	public Directory getDirectory() throws IOException {
		if(directory == null){
			File indexRepositoryFile = new File(this.indexDer);
	        Path path = indexRepositoryFile.toPath();
	        directory = FSDirectory.open(path);
		}
		return directory;
	}


	/*创建索引*/
    public void create(IndexObject indexObject) {

		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
			indexWriter = new IndexWriter(this.getDirectory(), config);
			indexWriter.addDocument(DocumentUtil.IndexObject2Document(indexObject));
			indexWriter.commit();
		 } catch (Exception e) {
			 e.printStackTrace();
			 try {
			 	indexWriter.rollback();
			 } catch (IOException e1) {
					e1.printStackTrace();
			 }
		 }finally {
			 try {
				 indexWriter.close();
			 } catch (IOException e1) {
				 e1.printStackTrace();
	    		}
			}
    	}


    /* 删除索引 */
	public void deleteAll() {
		IndexWriter indexWriter = null;
		try {
			IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
			indexWriter = new IndexWriter(this.getDirectory(), config);
			Long result =indexWriter.deleteAll();
			/*清空回收站*/
			indexWriter.forceMergeDeletes();
           log.info("deleted:{}",result);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				indexWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/* 更新单条索引 */
	public void update(IndexObject indexObject) {

		IndexWriter indexWriter = null;

		try {

			Term term = new Term("id", indexObject.getId().toString());
			IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
			indexWriter = new IndexWriter(this.getDirectory(), config);
			indexWriter.updateDocument(term,DocumentUtil.IndexObject2Document(indexObject));

		} catch (Exception e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				indexWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/* 查询索引 */
    public TableResultResponse<IndexObject> page(Integer pageNumber, Integer pageSize, String keyword){

		IndexReader indexReader = null;
		TableResultResponse<IndexObject> pageQuery = null;
		List<IndexObject> searchResults = new ArrayList<>();
		try {
			indexReader = DirectoryReader.open(this.getDirectory());
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			Query query = QueryUtil.query(keyword,this.getAnalyzer(),"title","descripton");
            ScoreDoc lastScoreDoc = this.getLastScoreDoc(pageNumber, pageSize, query, indexSearcher);
            /*将上一页的最后一个document传递给searchAfter方法以得到下一页的结果 */
            TopDocs topDocs = indexSearcher.searchAfter(lastScoreDoc,query, pageSize);
			Highlighter highlighter = this.addStringHighlighter(query);
			log.info("搜索词语：{}",keyword);
			log.info("总共的查询结果：{}", topDocs.totalHits);
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			    int docID = scoreDoc.doc;
			    float score = scoreDoc.score;
			    Document document = indexSearcher.doc(docID);
			    IndexObject indexObject =DocumentUtil.document2IndexObject(this.getAnalyzer(), highlighter, document,score);
				searchResults.add(indexObject);
			    log.info("相关度得分：" + score);
			}
			Collections.sort(searchResults);
			pageQuery = new TableResultResponse<>(topDocs.totalHits,searchResults);

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				indexReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pageQuery;
	}

    /* 根据页码和分页大小获取上一次的最后一个ScoreDoc */
    private ScoreDoc getLastScoreDoc(Integer pageNumber,Integer pageSize,Query query,IndexSearcher searcher) throws IOException {
        if(pageNumber==1) {
			return null;
		}
        int total = pageSize*(pageNumber-1);
        TopDocs topDocs = searcher.search(query,total);
        return topDocs.scoreDocs[total -1];
    }


	/* 设置字符串高亮 */
	private Highlighter addStringHighlighter(Query query){
        QueryScorer scorer=new QueryScorer(query);
        Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<font color='red'>","</font>");
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		return highlighter;
	}


	public void delete(IndexObject indexObject) {
		IndexWriter indexWriter = null;
		try {
			Term term = new Term("id", indexObject.getId().toString());
			IndexWriterConfig config = new IndexWriterConfig(this.getAnalyzer());
			indexWriter = new IndexWriter(this.getDirectory(), config);
			indexWriter.deleteDocuments(term);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				indexWriter.rollback();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				indexWriter.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
