package pt.iscte.pidesco.codegenerator;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;


public class CodeVisitor extends ASTVisitor{
	
	ArrayList<FieldDeclaration> fields = new ArrayList<FieldDeclaration>();
	ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
	ArrayList<Assignment> assignments = new ArrayList<Assignment>();
	
	@Override
	public boolean visit(FieldDeclaration node) {
		fields.add(node);
		return false; // false to avoid child VariableDeclarationFragment to be processed again
	}
	
	// visits assignments (=, +=, etc)
	@Override
	public boolean visit(Assignment node) {
		assignments.add(node);
		return true;
	}
	
	// visits methods 
	@Override
	public boolean visit(MethodDeclaration node) {
		methods.add(node);
		return true;
	}
	
	public ArrayList<FieldDeclaration> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldDeclaration> fields) {
		this.fields = fields;
	}

	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}

	public void setMethods(ArrayList<MethodDeclaration> methods) {
		this.methods = methods;
	}

	public ArrayList<Assignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(ArrayList<Assignment> assignments) {
		this.assignments = assignments;
	}
	
}
