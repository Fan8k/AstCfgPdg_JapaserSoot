package cn.fan.ast.visitor;

import java.util.HashMap;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitor;

/**
 * 遍历整棵树
 * @author fan
 *
 */
public class WholeVisitor implements VoidVisitor<StringBuilder> {
	private HashMap<Node, Integer> NodeToInteger;

	public WholeVisitor() {
		NodeToInteger = new HashMap<Node, Integer>(16);
	}

	@Override
	public void visit(NodeList n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AnnotationDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AnnotationMemberDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ArrayAccessExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ArrayCreationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ArrayCreationLevel n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ArrayInitializerExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ArrayType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AssertStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AssignExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BinaryExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BlockComment n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BlockStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BooleanLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(BreakStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CastExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CatchClause n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CharLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ClassExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ClassOrInterfaceDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ClassOrInterfaceType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CompilationUnit n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ConditionalExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ConstructorDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ContinueStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DoStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DoubleLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EmptyStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EnclosedExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EnumConstantDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(EnumDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExplicitConstructorInvocationStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExpressionStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FieldAccessExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FieldDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ForStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ForeachStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IfStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ImportDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(InitializerDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(InstanceOfExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IntegerLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(IntersectionType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(JavadocComment n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LabeledStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LambdaExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LineComment n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LocalClassDeclarationStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(LongLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MarkerAnnotationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MemberValuePair n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MethodCallExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MethodDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MethodReferenceExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NameExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Name n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NormalAnnotationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NullLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ObjectCreationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(PackageDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Parameter n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(PrimitiveType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReturnStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SimpleName n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SingleMemberAnnotationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(StringLiteralExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SuperExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SwitchEntryStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SwitchStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(SynchronizedStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ThisExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ThrowStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TryStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TypeExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(TypeParameter n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(UnaryExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(UnionType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(UnknownType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VariableDeclarationExpr n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VariableDeclarator n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VoidType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(WhileStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(WildcardType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleDeclaration n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleRequiresStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleExportsStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleProvidesStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleUsesStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ModuleOpensStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(UnparsableStmt n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReceiverParameter n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VarType n, StringBuilder arg) {
		// TODO Auto-generated method stub

	}

}
