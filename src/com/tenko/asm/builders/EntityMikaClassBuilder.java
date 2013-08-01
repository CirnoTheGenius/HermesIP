package com.tenko.asm.builders;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.tenko.FriendlyWall;

public class EntityMikaClassBuilder implements Opcodes {

	//REMEMBER TO ALWAYS REPLACE " + FriendlyWall.getCraftVersion() + " WITH " + FriendlyWall.getCraftVersion() + ".

	private int Version = V1_7;
	private ASMClassLoader loader;
	private Class<?> entityClass;

	public EntityMikaClassBuilder(){
		loadClassesIntoMemory();
	}

	public void loadClassesIntoMemory(){
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;
		FieldVisitor fv;
		loader = new ASMClassLoader(new URL[]{}, this.getClass().getClassLoader());

		byte[] bytesServerNetworkManager;
		byte[] bytesServerConnection;
		byte[] bytesIMika;
		byte[] bytesEntityMika;

		//Here's the 6-foot behemth of source code just to dynmically load classes.
		cw.visit(Version, ACC_PUBLIC + ACC_SUPER, "com/tenko/asm/network/ServerNetworkManager", null, "java/lang/Object", new String[] { "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/INetworkManager" });

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(7, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "a", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(10, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(0, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "a", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Connection;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(13, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitLocalVariable("arg0", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Connection;", null, l0, l1, 1);
			mv.visitMaxs(0, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_VARARGS, "a", "(Ljava/lang/String;[Ljava/lang/Object;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(16, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitLocalVariable("arg0", "Ljava/lang/String;", null, l0, l1, 1);
			mv.visitLocalVariable("arg1", "[Ljava/lang/Object;", null, l0, l1, 2);
			mv.visitMaxs(0, 3);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "b", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(19, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(0, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "d", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(22, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(0, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "e", "()I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(26, l0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getSocketAddress", "()Ljava/net/SocketAddress;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(31, l0);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "queue", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(35, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerNetworkManager;", null, l0, l1, 0);
			mv.visitLocalVariable("arg0", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;", null, l0, l1, 1);
			mv.visitMaxs(0, 2);
			mv.visitEnd();
		}
		cw.visitEnd();

		bytesServerNetworkManager = cw.toByteArray();
		cw = new ClassWriter(0);

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/tenko/asm/network/ServerConnection", null, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerConnection", null);

		cw.visitSource("ServerConnection.java", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(9, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESTATIC, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/MinecraftServer", "getServer", "()Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/MinecraftServer;");
			mv.visitTypeInsn(NEW, "com/tenko/asm/network/ServerNetworkManager");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "com/tenko/asm/network/ServerNetworkManager", "<init>", "()V");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerConnection", "<init>", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/MinecraftServer;Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/INetworkManager;Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer;)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(10, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l2, 0);
			mv.visitLocalVariable("entityplayer", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer;", null, l0, l2, 1);
			mv.visitMaxs(4, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "a", "()Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(14, l0);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "a", "(DDDFF)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(18, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitLocalVariable("d0", "D", null, l0, l1, 1);
			mv.visitLocalVariable("d1", "D", null, l0, l1, 3);
			mv.visitLocalVariable("d2", "D", null, l0, l1, 5);
			mv.visitLocalVariable("f", "F", null, l0, l1, 7);
			mv.visitLocalVariable("f1", "F", null, l0, l1, 8);
			mv.visitMaxs(0, 9);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "b", "()Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(22, l0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "onUnhandledPacket", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(26, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitLocalVariable("p", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;", null, l0, l1, 1);
			mv.visitMaxs(0, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "sendPacket", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(29, l0);
			mv.visitInsn(RETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitLocalVariable("p", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Packet;", null, l0, l1, 1);
			mv.visitMaxs(0, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "lowPriorityCount", "()I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(33, l0);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/network/ServerConnection;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		cw.visitEnd();

		bytesServerConnection = cw.toByteArray();
		cw = new ClassWriter(0);

		cw.visit(V1_7, 0x601, "com/tenko/asm/entity/IMika", null, "java/lang/Object", null);

		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "spawnIn", "(Ljava/lang/Object;)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "lookAt", "(Lorg/bukkit/Location;)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "chat", "(Lorg/bukkit/entity/Player;)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getQuotes", "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Ljava/lang/String;>;", null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getYaw", "(Lorg/bukkit/util/Vector;)F", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getName", "()Ljava/lang/String;", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getCraftBukkitLocation", "()Lorg/bukkit/Location;", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "setPositionRotation", "(DDDFF)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "die", "()V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "teleportTo", "(Lorg/bukkit/Location;)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getCraftEntity", "()Lorg/bukkit/entity/LivingEntity;", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "getOptions", "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Lcom/tenko/visualnovel/Option;>;", null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "isTalking", "()Z", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "setTalking", "(Z)V", null, null);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC + ACC_ABSTRACT, "chat", "(Lorg/bukkit/entity/Player;Ljava/lang/String;)V", null, null);
			mv.visitEnd();
		}
		cw.visitEnd();

		bytesIMika = cw.toByteArray();
		cw = new ClassWriter(0);

		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "com/tenko/asm/entity/EntityMika", null, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer", new String[] { "com/tenko/asm/entity/IMika" });

		{
			fv = cw.visitField(ACC_PRIVATE, "keyNovel", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Lcom/tenko/visualnovel/Option;>;", null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "quotes", "Ljava/util/ArrayList;", "Ljava/util/ArrayList<Ljava/lang/String;>;", null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "chatIndex", "S", null, null);
			fv.visitEnd();
		}
		{
			fv = cw.visitField(ACC_PRIVATE, "visualNovelTalking", "Z", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/String;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(24, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/MinecraftServer");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitTypeInsn(NEW, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerInteractManager");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerInteractManager", "<init>", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World;)V");
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer", "<init>", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/MinecraftServer;Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World;Ljava/lang/String;Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerInteractManager;)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(18, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "keyNovel", "Ljava/util/ArrayList;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(19, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "java/util/ArrayList");
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V");
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "quotes", "Ljava/util/ArrayList;");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(20, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(21, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "visualNovelTalking", "Z");
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(25, l5);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "com/tenko/asm/network/ServerConnection");
			mv.visitInsn(DUP);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "com/tenko/asm/network/ServerConnection", "<init>", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer;)V");
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "playerConnection", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerConnection;");
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(26, l6);
			mv.visitMethodInsn(INVOKESTATIC, "com/tenko/functions/NPCs", "getNPCList", "()Ljava/util/HashMap;");
			mv.visitVarInsn(ALOAD, 3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/HashMap", "put", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
			mv.visitInsn(POP);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(28, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "playerInteractManager", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerInteractManager;");
			mv.visitFieldInsn(GETSTATIC, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EnumGamemode", "SURVIVAL", "Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EnumGamemode;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/PlayerInteractManager", "setGameMode", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EnumGamemode;)V");
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(29, l8);
			mv.visitInsn(RETURN);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l9, 0);
			mv.visitLocalVariable("ms", "Ljava/lang/Object;", null, l0, l9, 1);
			mv.visitLocalVariable("w", "Ljava/lang/Object;", null, l0, l9, 2);
			mv.visitLocalVariable("naem", "Ljava/lang/String;", null, l0, l9, 3);
			mv.visitMaxs(7, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getName", "()Ljava/lang/String;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(33, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "name", "Ljava/lang/String;");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getCraftBukkitLocation", "()Lorg/bukkit/Location;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(38, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/entity/EntityMika", "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer", "getLocation", "()Lorg/bukkit/Location;");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "spawnIn", "(Ljava/lang/Object;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(43, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(CHECKCAST, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/World", "addEntity", "(Lnet/minecraft/server/" + FriendlyWall.getCraftVersion() + "/Entity;)Z");
			mv.visitInsn(POP);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(44, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitLocalVariable("w", "Ljava/lang/Object;", null, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "lookAt", "(Lorg/bukkit/Location;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			Label l1 = new Label();
			Label l2 = new Label();
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/IllegalArgumentException");
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/IllegalAccessException");
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/NoSuchFieldException");
			mv.visitTryCatchBlock(l0, l1, l2, "java/lang/SecurityException");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(48, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/Location", "toVector", "()Lorg/bukkit/util/Vector;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/entity/EntityMika", "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer", "getEyeLocation", "()Lorg/bukkit/Location;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/Location", "toVector", "()Lorg/bukkit/util/Vector;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/util/Vector", "subtract", "(Lorg/bukkit/util/Vector;)Lorg/bukkit/util/Vector;");
			mv.visitVarInsn(ASTORE, 2);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(49, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/entity/EntityMika", "getYaw", "(Lorg/bukkit/util/Vector;)F");
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "yaw", "F");
			mv.visitLabel(l0);
			mv.visitLineNumber(51, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
			mv.visitFieldInsn(GETSTATIC, "com/tenko/functions/NPCs", "field", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getField", "(Ljava/lang/String;)Ljava/lang/reflect/Field;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "yaw", "F");
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Field", "set", "(Ljava/lang/Object;Ljava/lang/Object;)V");
			mv.visitLabel(l1);
			mv.visitLineNumber(52, l1);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l2);
			mv.visitLineNumber(53, l2);
			mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {"com/tenko/asm/entity/EntityMika", "org/bukkit/Location", "org/bukkit/util/Vector"}, 1, new Object[] {"java/lang/Exception"});
			mv.visitVarInsn(ASTORE, 3);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(54, l6);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V");
			mv.visitLabel(l5);
			mv.visitLineNumber(56, l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/Location", "getY", "()D");
			mv.visitLdcInsn(new Double("65.0"));
			mv.visitInsn(DSUB);
			mv.visitInsn(D2F);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "pitch", "F");
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(57, l7);
			mv.visitInsn(RETURN);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l3, l8, 0);
			mv.visitLocalVariable("loc", "Lorg/bukkit/Location;", null, l3, l8, 1);
			mv.visitLocalVariable("v", "Lorg/bukkit/util/Vector;", null, l4, l8, 2);
			mv.visitLocalVariable("e", "Ljava/lang/Exception;", null, l6, l5, 3);
			mv.visitMaxs(5, 4);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "chat", "(Lorg/bukkit/entity/Player;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(61, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "quotes", "Ljava/util/ArrayList;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I");
			Label l1 = new Label();
			mv.visitJumpInsn(IFLE, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(62, l2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("<");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "RED", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "name", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "WHITE", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitLdcInsn("> ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "quotes", "Ljava/util/ArrayList;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "get", "(I)Ljava/lang/Object;");
			mv.visitTypeInsn(CHECKCAST, "java/lang/String");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "sendMessage", "(Ljava/lang/String;)V");
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(63, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IADD);
			mv.visitInsn(I2S);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(64, l4);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "quotes", "Ljava/util/ArrayList;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "size", "()I");
			Label l5 = new Label();
			mv.visitJumpInsn(IF_ICMPLT, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(65, l6);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitInsn(ICONST_0);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "chatIndex", "S");
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(67, l7);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitJumpInsn(GOTO, l5);
			mv.visitLabel(l1);
			mv.visitLineNumber(68, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("<");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "RED", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "name", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "WHITE", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitLdcInsn("> Hi, ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "getName", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitLdcInsn("!");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "sendMessage", "(Ljava/lang/String;)V");
			mv.visitLabel(l5);
			mv.visitLineNumber(70, l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(RETURN);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l8, 0);
			mv.visitLocalVariable("plyr", "Lorg/bukkit/entity/Player;", null, l0, l8, 1);
			mv.visitMaxs(4, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getQuotes", "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Ljava/lang/String;>;", null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(74, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "quotes", "Ljava/util/ArrayList;");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getOptions", "()Ljava/util/ArrayList;", "()Ljava/util/ArrayList<Lcom/tenko/visualnovel/Option;>;", null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(79, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "keyNovel", "Ljava/util/ArrayList;");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getYaw", "(Lorg/bukkit/util/Vector;)F", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(84, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/util/Vector", "getX", "()D");
			mv.visitVarInsn(DSTORE, 2);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(85, l1);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/util/Vector", "getZ", "()D");
			mv.visitVarInsn(DSTORE, 4);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(86, l2);
			mv.visitInsn(DCONST_0);
			mv.visitVarInsn(DSTORE, 6);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(88, l3);
			mv.visitVarInsn(DLOAD, 2);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCMPL);
			Label l4 = new Label();
			mv.visitJumpInsn(IFEQ, l4);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(90, l5);
			mv.visitVarInsn(DLOAD, 2);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCMPG);
			Label l6 = new Label();
			mv.visitJumpInsn(IFGE, l6);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(91, l7);
			mv.visitLdcInsn(new Double("4.71238898038469"));
			mv.visitVarInsn(DSTORE, 6);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(92, l8);
			Label l9 = new Label();
			mv.visitJumpInsn(GOTO, l9);
			mv.visitLabel(l6);
			mv.visitLineNumber(93, l6);
			mv.visitFrame(Opcodes.F_APPEND,3, new Object[] {Opcodes.DOUBLE, Opcodes.DOUBLE, Opcodes.DOUBLE}, 0, null);
			mv.visitLdcInsn(new Double("1.5707963267948966"));
			mv.visitVarInsn(DSTORE, 6);
			mv.visitLabel(l9);
			mv.visitLineNumber(95, l9);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(DLOAD, 6);
			mv.visitVarInsn(DLOAD, 4);
			mv.visitVarInsn(DLOAD, 2);
			mv.visitInsn(DDIV);
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "atan", "(D)D");
			mv.visitInsn(DSUB);
			mv.visitVarInsn(DSTORE, 6);
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(96, l10);
			Label l11 = new Label();
			mv.visitJumpInsn(GOTO, l11);
			mv.visitLabel(l4);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(DLOAD, 4);
			mv.visitInsn(DCONST_0);
			mv.visitInsn(DCMPG);
			mv.visitJumpInsn(IFGE, l11);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(97, l12);
			mv.visitLdcInsn(new Double("3.141592653589793"));
			mv.visitVarInsn(DSTORE, 6);
			mv.visitLabel(l11);
			mv.visitLineNumber(99, l11);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(DLOAD, 6);
			mv.visitInsn(DNEG);
			mv.visitLdcInsn(new Double("180.0"));
			mv.visitInsn(DMUL);
			mv.visitLdcInsn(new Double("3.141592653589793"));
			mv.visitInsn(DDIV);
			mv.visitInsn(D2F);
			mv.visitInsn(FRETURN);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l13, 0);
			mv.visitLocalVariable("motion", "Lorg/bukkit/util/Vector;", null, l0, l13, 1);
			mv.visitLocalVariable("dx", "D", null, l1, l13, 2);
			mv.visitLocalVariable("dz", "D", null, l2, l13, 4);
			mv.visitLocalVariable("yawz", "D", null, l3, l13, 6);
			mv.visitMaxs(6, 8);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "setPositionRotation", "(DDDFF)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(104, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(DLOAD, 1);
			mv.visitVarInsn(DLOAD, 3);
			mv.visitVarInsn(DLOAD, 5);
			mv.visitVarInsn(FLOAD, 7);
			mv.visitVarInsn(FLOAD, 8);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer", "setPositionRotation", "(DDDFF)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(105, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitLocalVariable("x", "D", null, l0, l2, 1);
			mv.visitLocalVariable("y", "D", null, l0, l2, 3);
			mv.visitLocalVariable("z", "D", null, l0, l2, 5);
			mv.visitLocalVariable("yaw", "F", null, l0, l2, 7);
			mv.visitLocalVariable("pitch", "F", null, l0, l2, 8);
			mv.visitMaxs(9, 9);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "die", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(109, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/server/" + FriendlyWall.getCraftVersion() + "/EntityPlayer", "die", "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(110, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "teleportTo", "(Lorg/bukkit/Location;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(114, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/entity/EntityMika", "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "org/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer", "teleport", "(Lorg/bukkit/Location;)Z");
			mv.visitInsn(POP);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(115, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitLocalVariable("l", "Lorg/bukkit/Location;", null, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "getCraftEntity", "()Lorg/bukkit/entity/LivingEntity;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(119, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKEVIRTUAL, "com/tenko/asm/entity/EntityMika", "getBukkitEntity", "()Lorg/bukkit/craftbukkit/" + FriendlyWall.getCraftVersion() + "/entity/CraftPlayer;");
			mv.visitInsn(ARETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "isTalking", "()Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(124, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "visualNovelTalking", "Z");
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "setTalking", "(Z)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(129, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "com/tenko/asm/entity/EntityMika", "visualNovelTalking", "Z");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(130, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitLocalVariable("a", "Z", null, l0, l2, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "chat", "(Lorg/bukkit/entity/Player;Ljava/lang/String;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(134, l0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
			mv.visitInsn(DUP);
			mv.visitLdcInsn("<");
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "RED", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "com/tenko/asm/entity/EntityMika", "name", "Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitFieldInsn(GETSTATIC, "org/bukkit/ChatColor", "WHITE", "Lorg/bukkit/ChatColor;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuilder;");
			mv.visitLdcInsn("> ");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
			mv.visitMethodInsn(INVOKEINTERFACE, "org/bukkit/entity/Player", "sendMessage", "(Ljava/lang/String;)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(135, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", "Lcom/tenko/asm/entity/EntityMika;", null, l0, l2, 0);
			mv.visitLocalVariable("plyr", "Lorg/bukkit/entity/Player;", null, l0, l2, 1);
			mv.visitLocalVariable("message", "Ljava/lang/String;", null, l0, l2, 2);
			mv.visitMaxs(4, 3);
			mv.visitEnd();
		}
		cw.visitEnd();

		bytesEntityMika = cw.toByteArray();

		//		byte[] bytesServerNetworkManager;
		//		byte[] bytesServerConnection;
		//		byte[] bytesIMika;
		//		byte[] bytesEntityMika;
		defineClass("com.tenko.asm.network.ServerNetworkManager", bytesServerNetworkManager);
		defineClass("com.tenko.asm.network.ServerConnection", bytesServerConnection);
		defineClass("com.tenko.asm.entity.IMika", bytesIMika);
		defineClass("com.tenko.asm.entity.EntityMika", bytesEntityMika);
		try {
			entityClass = loader.findClass("com.tenko.asm.entity.EntityMika");
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public void defineClass(String s, byte[] b){
		loader.addClass(s, b);
	}

	public Object generateInstance(Object minecraft, Object world, String name){
		if(entityClass == null){
			loadClassesIntoMemory();
			if(entityClass == null){
				Bukkit.broadcastMessage(ChatColor.RED + "NPC's are apperently broken. Tell Tenko \"The entity class is null\".");
			}
		}

		try {
			return entityClass.getConstructor(Object.class, Object.class, String.class).newInstance(minecraft, world, name);
		} catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e){
			e.printStackTrace();
		}

		return null;
	}

	//REMEMBER TO ALWAYS REPLACE v1_6__R2 WITH " + FriendlyWall.getCraftVersion() + ".

}
