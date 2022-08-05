$('.jstree').jstree();   
$(".jstree").on("select_node.jstree", function (e, data) {  
			
		// console.log(e,data,data.selected);
    if(data.node.id !=1 ){ 
        data.instance.toggle_node(data.node);
    }  
      
}); 