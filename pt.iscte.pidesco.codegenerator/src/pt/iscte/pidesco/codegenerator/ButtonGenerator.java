package pt.iscte.pidesco.codegenerator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pt.iscte.pidesco.codegenerator.extensibility.RangeScope;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class ButtonGenerator {
	
	private Button justButton;
	private Composite viewArea;
	
	//Constructor used to instantiate a button with the name of the text and placed on the viewArea.
	private ButtonGenerator(Composite viewArea, String text) {
		justButton = new Button(viewArea, SWT.VERTICAL);
		justButton.setText(text);
		this.viewArea = viewArea;
	}
	
	//Function used to give the button "Generate code" its listener with the functionalities
	//of generating code depending on the text selected.
	public static void addGenerateCode(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					JSONObject object = getUserCode(sel.getText());
					if(object != null) {
						RangeScope scopeRange = RangeScope.valueOf(object.get("scope").toString());
						if(checkIfCursorIsInRange(new File("C:\\Users\\z004004j\\eclipse-workspace\\pt.iscte.pidesco.codegenerator\\src\\pt\\iscte\\pidesco\\codegenerator\\user_code.json"), editorServ, scopeRange)) {
							String code = (String) object.get("code");
							ClearSelected(editorServ);
							editorServ.insertTextAtCursor(code);
							editorServ.saveFile(f);
						}else {
							System.out.println("Out of range!");
						}
					}else if((sel.getText() + ".java").equals(f.getName())) {
						String name = sel.getText();
						ClearSelected(editorServ);
						String text = "\n\tpublic " + name + "(){" + "\n\n\t}";
						editorServ.insertTextAtCursor(text);
						editorServ.saveFile(f);
					}else if((sel.getText()).equals("main")) {
						ClearSelected(editorServ);
						String text = "\n\tpublic static void main(String[] args){\n\n\t}";
						editorServ.insertTextAtCursor(text);
						editorServ.saveFile(f);
					}else if((sel.getText()).equals("sysout")) {
						ClearSelected(editorServ);
						String text = "\n\tSystem.out.println();";
						editorServ.insertTextAtCursor(text);
						editorServ.saveFile(f);
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate code" its listener with the 
	//ability to create setters and getters from the open file.
	public static void addGettersAndSetters(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					if(!visitor.fields.isEmpty()) {
						boolean firstSetter = true;
						for(FieldDeclaration field: visitor.getFields()) {
							String[] splitted = field.toString().replace(";", "").split(" ");
							GenerateSetter(firstSetter,  splitted[splitted.length-1].replaceAll("\n", ""), splitted[splitted.length-2], f);
							if(firstSetter = true) firstSetter = false; 
							GenerateGetter(splitted[splitted.length-1].replaceAll("\n", ""), splitted[splitted.length-2], f);
						}
					}
					editorServ.saveFile(f);
					buttonGenerator.viewArea.layout();
				}
			}
			
			private void GenerateSetter(boolean firstSetter, String fieldName,  String fieldType, File f) {
				String extraTab = "";
				if(!firstSetter) extraTab = "\t";
				editorServ.insertTextAtCursor(extraTab + "public void set" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()) + "(" + fieldType + " " + fieldName + "){ \n\t\tthis." + fieldName + "=" + fieldName + ";\n\t}\n\n");
			}
	
			private void GenerateGetter(String fieldName, String fieldType, File f) {
				editorServ.insertTextAtCursor("\tpublic " + fieldType + " get" + fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase()) + "(){ \n\t\treturn this." + fieldName + "; \n\t}\n\n");
			}
	
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}
	
	//Function used to give the button "Comment/Uncomment block of code" its 
	//listener with the ability to comment and uncomment on the text selected.
	public static void addUncommentComment(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					String textSelected = sel.getText();
					String text = sel.getText().replaceAll("\t", "").replaceAll("\n", "");
					ClearSelected(editorServ);
					if(!text.startsWith("/*") && !text.endsWith("*/")) {
						editorServ.insertTextAtCursor("/*" + textSelected + "*/");
					}else {
						textSelected = textSelected.replace("/*", "").replace("*/", "");
						editorServ.insertTextAtCursor(textSelected);
					}
					editorServ.saveFile(f);
					buttonGenerator.viewArea.layout();
				}
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate toString()" its 
	//listener with the ability to create a toString function with the name of the class
	//and its variables.
	public static void addToString(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					String fileName = f.getName().replaceAll(".java", "");
					String toString = "@Override\n\tpublic String toString(){\n\t\treturn \""+ fileName + " [";
					if(!visitor.getFields().isEmpty()) {
						for(int i = 0; i< visitor.getFields().size(); i++) {
							String[] splitted = visitor.getFields().get(i).toString().replace(";", "").split(" ");
							String fieldName = splitted[splitted.length - 1].replace("\n", "");
							toString += fieldName + "=\" + " + fieldName + " + \"";
							if(i!=visitor.getFields().size()-1) {
								toString += ", ";
							}
						}
					}
					toString += "]\";\n\t}";
					ClearSelected(editorServ);
					editorServ.insertTextAtCursor(toString);
					editorServ.saveFile(f);
					buttonGenerator.viewArea.layout();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
	}
	
	//Function used to give the button "Generate Constructor using Fields..." its 
	//listener with the to open a window and the user will selected which
	//fields wants to takes part on creating the constructor method.
	public static void addConstructorFields(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					CodeVisitor visitor = new CodeVisitor();
					editorServ.parseFile(f, visitor);
					if(!visitor.getFields().isEmpty()) {
						String className = f.getName().replace(".java", "");
						String statement = "public " + className + "(";
						String setValues = "){\n\t\t ";
						for(FieldDeclaration field: visitor.getFields()) {
							String[] splitted = field.toString().replace(";", "").replace("\n", "").split(" ");
							statement += splitted[splitted.length-2] + " " + splitted[splitted.length-1];
							if(!field.equals(visitor.getFields().get(visitor.getFields().size()-1))){
								statement += ",";
							}
							setValues += "this." + splitted[splitted.length-1] + "=" + splitted[splitted.length-1] + ";\n";
						}
						
						statement = statement + setValues + "\t\t}";
						editorServ.insertTextAtCursor(text);
						//ConstructorWindow cWindow = new ConstructorWindow(visitor.fields);
					}
					editorServ.saveFile(f);
					buttonGenerator.viewArea.layout();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}			
		});
		
		
	}
	
	//Function used to give the button "Surround with..." its 
	//listener with the ability to surround the selected statement with a try/catch block.
	public static void addSurroundWith(Composite viewArea, String text, JavaEditorServices editorServ) {
		ButtonGenerator buttonGenerator = new ButtonGenerator(viewArea, text);
		buttonGenerator.justButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				File f = editorServ.getOpenedFile();
				if (f != null) {
					ITextSelection sel = editorServ.getTextSelected(f);
					if(sel.getText().isEmpty()) {
						System.out.println("Select a statment...");
					}else{
						CodeVisitor visitor = new CodeVisitor();
						editorServ.parseFile(f, visitor);
						String text = "try {\n\t\t\t" + sel.getText() + "\n\t\t}catch(Exception e){ \n \t\t\t// TODO Auto-generated catch block\n\t\t\te.printStackTrace();\n\t\t}";
						ClearSelected(editorServ);
						editorServ.insertTextAtCursor(text);
					}
				}
				editorServ.saveFile(f);
				buttonGenerator.viewArea.layout();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		
	}
	
	private static boolean checkIfCursorIsInRange(File file, JavaEditorServices editorServ, RangeScope scope) {
		int cursorPosition = editorServ.getCursorPosition();
		CodeVisitor visitor = new CodeVisitor();
		editorServ.parseFile(file, visitor);
		switch(scope.toString()) {
			case "ALL":
				return true;
			case "INSIDECLASS":
				ClassPosition classPosition = getClassPosition(file);
				if(cursorPosition > classPosition.startClassPosition && cursorPosition < classPosition.endClassPosition) {
					return true;
				}
				return false;
			case "OUTOFCLASS":
				classPosition = getClassPosition(file);
				if(cursorPosition < classPosition.startClassPosition && cursorPosition > classPosition.endClassPosition) {
					return true;
				}
				return false;
			case "INSIDEMETHOD":
				if(visitor.methods.size() > 0) {
					for(MethodDeclaration method : visitor.methods) {
						if(method.getStartPosition() <= cursorPosition && method.getStartPosition() + method.getLength() >= cursorPosition) {
							return true;
						}
					}
				}
				return false;
			case "OUTSIDEMETHOD":
				if(visitor.methods.size() > 0) {
					for(MethodDeclaration method : visitor.methods) {
						if(method.getStartPosition() <= cursorPosition && method.getStartPosition() + method.getLength() >= cursorPosition) {
							return false;
						}
					}
				}
				return true;
			default:
				return false;
		}
	}
	
	private static JSONObject getUserCode(String macro) {
		try {
			JSONParser jsonParser = new JSONParser();	
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader("C:\\Users\\z004004j\\eclipse-workspace\\pt.iscte.pidesco.codegenerator\\src\\pt\\iscte\\pidesco\\codegenerator\\user_code.json"));
			JSONArray allMacros = (JSONArray) jsonObject.get("all_macros");
			for(int i = 0; i < allMacros.size(); i++) {
				JSONObject object = (JSONObject) allMacros.get(i);
				if(object.get("macro").toString().equals(macro)) {
					return object;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
	}
	
	
	private static ClassPosition getClassPosition(File file) {
		String fileName = file.getName();
		int startClassPosition = 0, endClassPosition = 0, totalCount = 0;
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNext()) {
				String line = sc.next();
				if(line.contains(fileName) && startClassPosition != 0) {
					startClassPosition = line.length();
				}else if(line.endsWith("}")) {
					endClassPosition = totalCount + line.length();
				}
				totalCount = totalCount + line.length(); 
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ClassPosition(startClassPosition, endClassPosition, totalCount);
	}
	
	//Function used to clear the text previously selected so it could be replaced.
	private static void ClearSelected(JavaEditorServices javaServ) {
		File f = javaServ.getOpenedFile();
		javaServ.insertText(f, "", javaServ.getTextSelected(f).getOffset(), javaServ.getTextSelected(f).getLength());
	}
	
	
	
}
