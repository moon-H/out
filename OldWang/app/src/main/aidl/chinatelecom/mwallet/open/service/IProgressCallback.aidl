package chinatelecom.mwallet.open.service;
interface IProgressCallback{

	void progress(String oprAid,String des,int progress);
	
	void notify(String resCode,String resDes);
}