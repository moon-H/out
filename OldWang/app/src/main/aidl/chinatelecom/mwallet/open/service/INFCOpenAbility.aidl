package chinatelecom.mwallet.open.service;
import chinatelecom.mwallet.open.service.IProgressCallback;
interface INFCOpenAbility{
	boolean checkDevice();
	String getSeid();
	String getPPSeid();
	boolean setAppletPPse(String aid);
	void supSecurityDomainOpr(String oprType,String ssdId);
	void gpacUpdateOpr(String oprType,String aid);
	/**
		oprType 操作类型  01下载   04删除  aid 应用实例aid
	**/
	void appletOpr(String oprType,String aid);
	void syncApplet(String aid);
	String checkUpdate();
	boolean register(String pwd);
	void registCallback(in IProgressCallback callback);
	void unRegistCallback(in IProgressCallback callback);
	String doUserLogin();
	String init(String spId,String packName);//初始化接口，绑定成功后调用
	void setAPPlet(in Map map);//设置applet 的一些一些参数  目前有 appletVersion（key） 应用版本号
	void closeService();//close app
	void registerAndLogin();//注册并且登陆
	String getAppletInfo(String aid);//获取applet信息
	//获取手机号
	String getUserTelNumberByIccid(String aid, String packageName);
	//更新ac 01 下载，02删除
	boolean updateAC(String oprType,String aid,String packageName);
	//用户鉴权
	String userAuth(String aid,String oprType,String appletData);
	String regist();
	String appletStore(String channelType,String keyword,String pageNumber);
	
}