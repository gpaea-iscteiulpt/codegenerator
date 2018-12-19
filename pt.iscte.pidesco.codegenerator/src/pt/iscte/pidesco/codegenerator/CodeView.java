package pt.iscte.pidesco.codegenerator;

import java.util.Map;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import pt.iscte.pidesco.codegenerator.internal.CodeGeneratorActivator;
import pt.iscte.pidesco.codegenerator.service.CodeGeneratorServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class CodeView implements PidescoView{
	
	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		viewArea.setLayout(new RowLayout(SWT.VERTICAL));
		BundleContext context = CodeGeneratorActivator.getContext();
		
		ServiceReference<JavaEditorServices> editorReference = context.getServiceReference(JavaEditorServices.class);
		JavaEditorServices editorServ = context.getService(editorReference);
	    
	    ButtonGenerator.addGenerateCode(viewArea, "Generate code", editorServ);
	    
		ButtonGenerator.addGettersAndSetters(viewArea, "Generate Getters and Setters...", editorServ);
		
		ButtonGenerator.addUncommentComment(viewArea, "Comment/Uncomment block of code", editorServ);
		
		ButtonGenerator.addToString(viewArea, "Generate toString()", editorServ);
		
		ButtonGenerator.addConstructorFields(viewArea, "Generate constructor with fields...", editorServ);

		ButtonGenerator.addSurroundWith(viewArea, "Suround with try/catch", editorServ);	
		
		ListGenerator.registerUserCode(viewArea);
		
<<<<<<< HEAD
//		ServiceReference<CodeGeneratorServices> codeReference = context.getServiceReference(CodeGeneratorServices.class);
//		CodeGeneratorServices codeServ = context.getService(codeReference);
	
=======
		ServiceReference<CodeGeneratorServices> codeReference = context.getServiceReference(CodeGeneratorServices.class);
		CodeGeneratorServices codeServ = context.getService(codeReference);
		
		Button b = new Button(viewArea, SWT.VERTICAL);
		b.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				codeServ.addFunction("C:\\Users\\z004004j\\eclipse-workspace\\Test\\src\\Car.java", AccessibilityType.PUBLIC, "Teste4", "String", false);				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
>>>>>>> branch 'master' of https://github.com/gpaea-iscteiulpt/codegenerator.git
	}
	
	
	
}
