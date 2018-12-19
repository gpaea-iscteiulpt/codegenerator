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
		UserCodeGenerator usg1 = new UserCodeGenerator(hm1, RangeScope.ALL);
		HashMap<String,String> hm2 = new HashMap<String,String>();
		hm2.put("teste2", "public void teste2(){\n\t\tSystem.out.println(\"Teste2\");\n\t}");
		UserCodeGenerator usg2 = new UserCodeGenerator(hm2, RangeScope.INSIDEMETHOD);
		HashMap<String,String> hm3 = new HashMap<String,String>();
		hm3.put("teste3", "public void teste3(){\n\t\tSystem.out.println(\"Teste3\");\n\t}");
		UserCodeGenerator usg3 = new UserCodeGenerator(hm3, RangeScope.OUTSIDEMETHOD);
		HashMap<String,String> hm4 = new HashMap<String,String>();
		hm4.put("teste4", "public void teste4(){\n\t\tSystem.out.println(\"Teste4\");\n\t}");
		UserCodeGenerator usg4 = new UserCodeGenerator(hm4, RangeScope.INSIDECLASS);
		HashMap<String,String> hm5 = new HashMap<String,String>();
		hm5.put("teste5", "public void teste5(){\n\t\tSystem.out.println(\"Teste\");\n\t}");			
		UserCodeGenerator usg5 = new UserCodeGenerator(hm5, RangeScope.OUTOFCLASS);
		list.add(usg1);
		list.add(usg2);
		list.add(usg3);
		list.add(usg4);
		list.add(usg5);
		return list;
	}
}
