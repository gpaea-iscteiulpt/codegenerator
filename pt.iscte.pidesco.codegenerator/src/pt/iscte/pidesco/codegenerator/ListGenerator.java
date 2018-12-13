package pt.iscte.pidesco.codegenerator;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pt.iscte.pidesco.codegenerator.extensibility.RangeScope;
import pt.iscte.pidesco.codegenerator.extensibility.UserCode;
import pt.iscte.pidesco.codegenerator.extensibility.UserCodeGenerator;

public class ListGenerator {

	private static List justList;
	private Composite viewArea;
	private static JFrame frameOpt;
	
	private ListGenerator(Composite viewArea) {
		justList = new List(viewArea, SWT.VERTICAL);
		this.viewArea = viewArea;
	}
	
	public static void registerUserCode(Composite viewArea) {
		ListGenerator listGenerator = new ListGenerator(viewArea);
		IExtensionRegistry reg = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = reg.getConfigurationElementsFor("pt.iscte.pidesco.codegenerator.snippets");
		for(IConfigurationElement e : elements) {
			try {
				UserCode action = (UserCode) e.createExecutableExtension("class");
				ArrayList<UserCodeGenerator> userCodeGenerator = (ArrayList) action.addUserCode();
				JSONParser jsonParser = new JSONParser();
				File userCode = new File("C:\\Users\\z004004j\\eclipse-workspace\\pt.iscte.pidesco.codegenerator\\src\\pt\\iscte\\pidesco\\codegenerator\\user_code.json");
				if(userCode.length() == 0) { addInitialInformation(userCode); }
				Object object = jsonParser.parse(new FileReader("C:\\Users\\z004004j\\eclipse-workspace\\pt.iscte.pidesco.codegenerator\\src\\pt\\iscte\\pidesco\\codegenerator\\user_code.json"));
				JSONObject jsonObject = (JSONObject) object;
				JSONArray allMacros = (JSONArray) jsonObject.get("all_macros");
				for(UserCodeGenerator ucg : userCodeGenerator) {
					RangeScope rangeScope = ucg.getRangeScope();
					for(Map.Entry<String, String> entry : ucg.getMacroToCode().entrySet()) {
					    String macro = entry.getKey();
					    String code = entry.getValue();
					    if(!checkUserCode(macro, allMacros)) {
					    	addUserCode(allMacros, macro, code, rangeScope);
					    }else {
					    	overwriteUserCode(allMacros, macro, code, rangeScope);
					    }
					}
				}
				
				FileWriter pw = new FileWriter("C:\\Users\\z004004j\\eclipse-workspace\\pt.iscte.pidesco.codegenerator\\src\\pt\\iscte\\pidesco\\codegenerator\\user_code.json");
				pw.append("{\"all_macros\":" + allMacros.toJSONString() + "}");
				pw.close();
				
			} catch (CoreException e1) {
				e1.printStackTrace();
			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void addUserCode(JSONArray allMacros, String macro, String code, RangeScope rangeScope) {
		JSONObject obj = new JSONObject();
		obj.put("macro", macro);
		obj.put("code", code);
		obj.put("scope", rangeScope.toString());
		allMacros.add(obj);
		justList.add("Macro: " + macro + " - Code: " + code.substring(0, 20) + " - Scope: " + rangeScope);
	}
	
	private static void overwriteUserCode(JSONArray allMacros, String macro, String code, RangeScope rangeScope) {
		if(frameOpt == null) {
			frameOpt = new JFrame();
		}
		frameOpt.setVisible(true);
		frameOpt.setAlwaysOnTop(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frameOpt.setLocation(dim.width/2-frameOpt.getSize().width/2, dim.height/2-frameOpt.getSize().height/2);
		JOptionPane optionPane = new JOptionPane();
		Object[] options = {"Yes", "Cancel"};
		int value = JOptionPane.showOptionDialog(frameOpt, "The following macro already exists: " + macro + ". Do you wish to overwrite it?", "Warning",
						JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
		if(value == 0) {
			for(int i = 0; i < allMacros.size(); i++) {
				JSONObject object = (JSONObject) allMacros.get(i);
				if(object.get("macro").toString().equals(macro)) {
					allMacros.remove(i);
					break;
				}
			}
			addUserCode(allMacros, macro, code, rangeScope);
		}else {
			System.out.println("Canceled.");
		}
		frameOpt.dispose();
	}
	
	private static boolean checkUserCode(String macro, JSONArray allMacros) {
		Iterator<JSONObject> iterator = allMacros.iterator();
		while(iterator.hasNext()) {
			if(iterator.next().get("macro").toString().equals(macro)) {
				return true;
			}
		}
        return false;
	}

	private static void addInitialInformation(File userCode) {
		try {
			FileWriter pw = new FileWriter(userCode);
			pw.write("{\"all_macros\":[]}");
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
