package com.yuncitys.smart.parking.app.biz;

import com.yuncitys.ag.core.context.BaseContextHandler;
import com.yuncitys.smart.parking.app.entity.AppUser;
import com.yuncitys.smart.parking.app.mapper.AppUserMapper;
import com.yuncitys.smart.parking.common.biz.BusinessBiz;
import com.yuncitys.smart.parking.common.constant.iotexper.SysConstant;
import com.yuncitys.smart.parking.common.util.BooleanUtil;
import com.yuncitys.smart.parking.common.util.Sha256PasswordEncoder;
import com.yuncitys.smart.parking.common.util.StringUtil;
import com.yuncitys.smart.parking.common.validator.ValidatorUtils;
import com.yuncitys.smart.parking.common.validator.group.AddGroup;
import org.springframework.stereotype.Service;

/**
 * @author Mr.AG
 *@version 2022-05-16 20:56:32
 * @email
 */
@Service
public class AppUserBiz extends BusinessBiz<AppUserMapper, AppUser> {
    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();
    /**
     * 修改密码
     * @param oldPass
     * @param newPass
     * @return
     */
    public Boolean changePassword(String oldPass, String newPass) {
        AppUser user = this.getUserByMobile(BaseContextHandler.getUsername());
        if (encoder.matches(oldPass, user.getPassword())) {
            String password = encoder.encode(newPass);
            user.setIsSettingPwd(SysConstant.IS_SETTING_PWD.YES);
            user.setPassword(password);
            this.updateSelectiveById(user);
            return true;
        }
        return false;
    }

    /**
     * 修改密码，不需要原始密码
     * @param newPass
     * @return
     */
    public Boolean changePassword(String newPass) {
        AppUser user = this.getUserByMobile(BaseContextHandler.getUsername());
        if(null == user){
            return false;
        }
        String password = encoder.encode(newPass);
        user.setIsSettingPwd(SysConstant.IS_SETTING_PWD.YES);
        user.setPassword(password);
        this.updateSelectiveById(user);

        return true;
    }

    public AppUser getUserByMobile(String mobile) {
        AppUser appUser = new AppUser();
        appUser.setMobile(mobile);
        return this.selectOne(appUser);
    }

    @Override
    public void insertSelective(AppUser entity) {
        ValidatorUtils.validateEntity(entity, AddGroup.class);
        String password = encoder.encode(entity.getPassword());
        entity.setId(StringUtil.uuid());
        entity.setPassword(password);
        entity.setName(entity.getMobile());
        entity.setTenantId(BaseContextHandler.getTenantID());
        entity.setIsDeleted(Integer.parseInt(BooleanUtil.BOOLEAN_FALSE));
        entity.setEnabledFlag(SysConstant.ENABLED_FLAG.Y);
        super.insertSelective(entity);
    }

    @Override
    public void deleteById(Object id) {
        AppUser appUser = this.selectById(id);
        appUser.setIsDeleted(Integer.parseInt(BooleanUtil.switchValue(BooleanUtil.BOOLEAN_FALSE.equals(appUser.getIsDeleted()))));
        this.updateSelectiveById(appUser);
    }
}
