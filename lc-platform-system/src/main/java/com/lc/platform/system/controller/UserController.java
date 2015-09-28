package com.lc.platform.system.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lc.platform.commons.ImageUtil;
import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.dao.PageBean;
import com.lc.platform.extjs.ExtUtil;
import com.lc.platform.extjs.JsonReader;
import com.lc.platform.extjs.QueryParams;
import com.lc.platform.spring.FileUploadException;
import com.lc.platform.system.SystemUtil;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.exception.EmailUniqueException;
import com.lc.platform.system.exception.UserNameUniqueException;
import com.lc.platform.system.service.UserService;

@Controller
@RequestMapping("/system/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody JsonReader readUsers(QueryParams queryParams)throws Exception{
		PageBean pageBean = ExtUtil.getPageBean(queryParams);
		userService.readUsersByPageInfo(pageBean);
		JsonReader jsonReader = ExtUtil.getJsonReader(pageBean);
		return jsonReader;
	}
	
	@RequestMapping(value="headinfo")
	public String headinfo()throws Exception{
		return "system/UserHeadInfo";
	}
	
	/**
	 * 上传用户编辑的头像信息
	 * @param headImage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="headImageConvert",method =RequestMethod.POST)
	public @ResponseBody String headImageConvert(MultipartFile headImage) throws Exception {
		if(headImage.getSize()>1048576){
			throw new FileUploadException(MessageUtil.getMessage("21001", new Object[]{1}));
		}
		if(headImage.getSize()==0){
			throw new FileUploadException(MessageUtil.getMessage("21002"));
		}
		String filename = headImage.getOriginalFilename().toLowerCase();
		if(!filename.matches("[^\\s]+(\\.(?i)(jpg|png|gif|bmp))$")){
			throw new FileUploadException(MessageUtil.getMessage("21003"));
		}
		byte[] headImageBytes = headImage.getBytes();
		return "data:image/png;base64,"+Base64.encodeBase64String(headImageBytes);
	}
	
	@RequestMapping(value="saveHeadPic",method = { RequestMethod.POST})
	public @ResponseBody Message headPic(int destWidth,int destHeight,int finalWidth,int finalHeight,String destx,String desty,String headImageBase64) throws Exception{
		int x = Integer.parseInt(destx.replaceAll("-|px", ""));
		int y = Integer.parseInt(desty.replaceAll("-|px", ""));
		String base64String = headImageBase64.substring(22);
		byte[] headImageBytes = Base64.decodeBase64(base64String);
		InputStream is = new ByteArrayInputStream(headImageBytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageUtil.abscut(is, os, x, y, destWidth, destHeight, finalWidth, finalHeight);
		String newHeadImageBase64 = "data:image/png;base64,"+Base64.encodeBase64String(os.toByteArray());
		String userId = SystemUtil.getCurrentUser().getUserId();
		SystemUtil.getCurrentUser().setHeadImage24(newHeadImageBase64);
		userService.updateUserHeadImage(userId,newHeadImageBase64);
		return MessageUtil.message(14012,newHeadImageBase64);
	}
	
	/**
	 * 批量删除用户信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="batchDelete",method = RequestMethod.POST)
	public @ResponseBody
	Message batchDelete(String[] ids) throws Exception {
		userService.deleteUser(ids);
		return MessageUtil.message(14007);
	}
	
	/**
	 * 创建用户信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="create",method = RequestMethod.POST)
	public @ResponseBody Message create(User user,String[]roleIds,String[] deptIds) throws Exception {
		userService.saveUser(user,roleIds,deptIds);
		return MessageUtil.message(14006, user.getUserId());
	}
	
	/**
	 * 更新用户信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="update",method = RequestMethod.POST)
	public @ResponseBody Message update(User user) throws Exception {
		userService.updateUser(user);
		return MessageUtil.message(14008, user.getUserId());
	}
	
	
	
	/**
	 * 检查用户信息
	 * @param username 用户名
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="checkedUserName",method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Object checkedUserName(String username) throws Exception {
		try {
			userService.checkUserNameUnique(username);
			return true;
		} catch (UserNameUniqueException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 检查电子邮箱信息
	 * @param email 邮箱
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="checkedEmail",method = {RequestMethod.POST,RequestMethod.GET})
	public @ResponseBody Object checkedEmail(String email) throws Exception {
		try {
			userService.checkEmailUnique(email);
			return true;
		} catch (EmailUniqueException e) {
			return e.getMessage();
		}
	}
	
	
	/**
	 * 更新用户角色信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="role/update",method = RequestMethod.POST)
	public @ResponseBody Message updateRole(String userIds,String roleIds,boolean grant) throws Exception {
		if(StringUtils.isNotBlank(userIds)){
			userService.updateUserRole(userIds.split(","),roleIds.split(","),grant);
		}
		return MessageUtil.message(14009);
	}
	
	/**
	 * 更新用户部门信息
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="dept/update",method = RequestMethod.POST)
	public @ResponseBody Message updateDept(String userIds,String deptId,boolean grant) throws Exception {
		if(StringUtils.isNotBlank(userIds)){
			userService.updateUserDept(userIds.split(","),deptId,grant);
		}
		return MessageUtil.message(14010);
	}
}
