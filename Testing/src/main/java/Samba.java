

import java.io.IOException;
import java.net.UnknownHostException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class Samba {

	public void test() throws SmbException, UnknownHostException, IOException {
		//		SambaUtils utils = new SambaUtils("ForShare", "Rel7.xPass!");
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "hercules", "Rel7.xPass!");
		String basePath = "smb://10.164.19.74/Distributor";
		//				+ "/export_files/Provisioning";
		SmbFile file = new SmbFile(basePath, auth);
		System.out.println(file.exists());
		//		SmbFile baseDir = new SmbFile(basePath, auth);
		//		if (!baseDir.exists()) {
		//			baseDir.mkdirs();
		//		}
		//		try (SmbFileOutputStream out = new SmbFileOutputStream(file, true)) {
		//			out.write("some dummy data".getBytes());
		//		}
	}
}
