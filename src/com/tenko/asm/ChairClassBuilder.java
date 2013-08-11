package com.tenko.asm;

import java.lang.reflect.Method;
import java.util.List;

import org.objectweb.asm.*;

import com.tenko.FriendlyWall;

public class ChairClassBuilder implements Opcodes {

	private Class<?> clazz;

	public ChairClassBuilder(){
		loadClassIntoMemory();
	}

	public void loadClassIntoMemory(){
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/tenko/asm/ChairWatcher", null, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/DataWatcher", null);

		cw.visitSource("ChairWatcher.java", null);

		{
			fv = cw.visitField(ACC_PRIVATE, "metadata", "B", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(B)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(12, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/DataWatcher", "<init>", "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(13, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/ChairWatcher", "metadata", "B");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(14, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/ChairWatcher;", null, l0, l3, 0);
			mv.visitLocalVariable("i", "B", null, l0, l3, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "b", "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Ljava/lang/Object;>;", null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(18, l0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
			mv.visitVarInsn(ASTORE, 1);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(19, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(NEW, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/WatchableObject");
			mv.visitInsn(DUP);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/ChairWatcher", "metadata", "B");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/WatchableObject", "<init>", "(IILjava/lang/Object;)V");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z");
			mv.visitInsn(POP);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(20, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ARETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/ChairWatcher;", null, l0, l3, 0);
			mv.visitLocalVariable("list", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Ljava/lang/Object;>;", l1, l3, 1);
			mv.visitMaxs(6, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "b", "()Ljava/util/List;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(1, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/ChairWatcher", "b", "()Ljava/util/ArrayList;");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		FriendlyClasses.loadClass("com.tenko.asm.ChairWatcher", cw.toByteArray());
		clazz = FriendlyClasses.getClass("com.tenko.asm.ChairWatcher");
	}

	public Object newWatcherInstance(byte b){
		try {
			return clazz.getConstructor(byte.class).newInstance(b);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<?> getListFromWatcher(Object watcher){
		try {
			Method m = watcher.getClass().getMethod("b");
			return (List<?>)m.invoke(watcher);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}