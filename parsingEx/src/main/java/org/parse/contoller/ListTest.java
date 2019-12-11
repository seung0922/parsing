package org.parse.contoller;

import java.io.File;
import java.io.IOException;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

public class ListTest {

	public static void main(String[] args) {
		
		GitHubClient client = new GitHubClient();
		
		client.setOAuth2Token("9a7545d6057560416cc35af4c375c6f3d23a6a7a");
		
		int cnt = 0;
		
		RepositoryService service = new RepositoryService();
		
		try {
			
			for (Repository repo : service.getRepositories("yunho0130")) {
			  System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());
			  System.out.println(cnt++);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	static void subDirList(String path) {
		
		File file = new File(path);

		File[] fileList = file.listFiles();

		try {
			for (File f : fileList) {
				if (f.isFile()) { // 파일일때
					
					int idx = f.getName().lastIndexOf('.');

					String lang = f.getName().substring(idx + 1);
					
					System.out.println(lang);
					
				} else if (f.isDirectory()) {
					// 하위 디렉토리 존재 시 다시 탐색
					subDirList(f.getCanonicalPath().toString());
				}
			}
			
		} catch(Exception e) {
			
		}
	}

}
