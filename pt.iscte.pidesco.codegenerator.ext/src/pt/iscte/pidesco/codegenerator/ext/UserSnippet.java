package pt.iscte.pidesco.codegenerator.ext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.iscte.pidesco.codegenerator.extensibility.RangeScope;
import pt.iscte.pidesco.codegenerator.extensibility.UserCode;
import pt.iscte.pidesco.codegenerator.extensibility.UserCodeGenerator;

public class UserSnippet implements UserCode{
	
	@Override
	public List<UserCodeGenerator> addUserCode() {
		ArrayList<UserCodeGenerator> list = new ArrayList<UserCodeGenerator>();
		HashMap<String,String> hm1 = new HashMap<String,String>(); 
		hm1.put("teste1", "public void teste1(){\n\t\tSystem.out.println(\"Teste1\");\n\t}");
		hm1.put("teste2", "public void teste2(){\n\t\tSystem.out.println(\"Teste2\");\n\t}");
		UserCodeGenerator usg1 = new UserCodeGenerator(hm1, RangeScope.ALL);		
		HashMap<String,String> hm2 = new HashMap<String,String>();
		hm2.put("teste3", "public void teste3(){\n\t\tSystem.out.println(\"Teste3\");\n\t}");
		UserCodeGenerator usg2 = new UserCodeGenerator(hm2, RangeScope.INSIDEMETHOD);
		list.add(usg1);
		list.add(usg2);
		return list;
	}
}
