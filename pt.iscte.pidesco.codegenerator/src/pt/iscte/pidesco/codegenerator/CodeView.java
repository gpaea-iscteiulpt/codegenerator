package pt.iscte.pidesco.codegenerator;

import java.util.ArrayList;
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
		
		TabFolder tabFolder = new TabFolder(viewArea, SWT.NONE);
		
		Composite codeGeneratorComposite = buildComposite(tabFolder, "Source", "This is tab show the basic functionalities granted with this plug-in.");
		Composite userCodeComposite = buildComposite(tabFolder, "Snippets", "This is tab shows the user snippets, implemented via extension-point.");
		
	    ButtonGenerator.addGenerateCode(codeGeneratorComposite, "Generate code", editorServ);
	    
		ButtonGenerator.addGettersAndSetters(codeGeneratorComposite, "Generate Getters and Setters...", editorServ);
		
		ButtonGenerator.addUncommentComment(codeGeneratorComposite, "Comment/Uncomment block of code", editorServ);
		
		ButtonGenerator.addToString(codeGeneratorComposite, "Generate toString()", editorServ);
		
		ButtonGenerator.addConstructorFields(codeGeneratorComposite, "Generate constructor with fields...", editorServ);

		ButtonGenerator.addSurroundWith(codeGeneratorComposite, "Suround with try/catch", editorServ);	
		
		ListGenerator.registerUserCode(userCodeComposite);
		
		
		ServiceReference<CodeGeneratorServices> codeReference = context.getServiceReference(CodeGeneratorServices.class);
		CodeGeneratorServices codeServ = context.getService(codeReference);
		
		Button b = new Button(codeGeneratorComposite, SWT.VERTICAL);
		b.setText("TESTAR SERVIÇOS");
		b.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				String path = "C:\\Users\\z004004j\\Desktop\\Projeto PA\\TestWorkspace\\src\\Car.java";
				AccessibilityType accessibility = AccessibilityType.PRIVATE;
				String fieldName = "fieldTeste";
				String fieldType = "String";
				boolean isStatic = true;
				String methodName = "setTeste";
				String returnType = "String";
				ArrayList<String> parameters = new ArrayList<String>();
				parameters.add("int testeInt");
				parameters.add("boolean testeBol");
				codeServ.addSettersAndGetters(path, parameters);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		

	}
	
	private Composite buildComposite(TabFolder tabFolder, String tabTitle, String tabToolTip) {
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
	    
	    TabItem tab = new TabItem(tabFolder, SWT.NONE);
	    tab.setText(tabTitle);
	    tab.setToolTipText(tabToolTip);
	    tab.setControl(composite);
   
	    return composite;
	}

	
}
