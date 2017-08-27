// ICTWalletService.aidl
package chinatelecom.mwallet.appservice;

// Declare any non-default types here with import statements

interface ICTWalletService {
	boolean sendAuthenticateResult(boolean authenticateSuccess, String operType, String oprAppletAID);
	String sendAuthenticateRequest(String operType, String oprAppletAID, String authenticateData, int resPackageType);
	String transmitNetwork(String operType, String oprAppletAID, String transmitData, String transmitFlow, int resPackageType);
}
