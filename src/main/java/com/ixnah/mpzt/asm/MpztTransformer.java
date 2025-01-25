package com.ixnah.mpzt.asm;

import com.ixnah.hmcl.api.AsmClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.security.ProtectionDomain;
import java.util.ListIterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class MpztTransformer implements AsmClassTransformer {
    @Override
    public boolean transform(ClassLoader classLoader, String className, ProtectionDomain protectionDomain, ClassNode classNode) {
        AtomicBoolean modify = new AtomicBoolean();
        if (className.equals("org/jackhuang/hmcl/ui/main/RootPage$Skin")) {
            classNode.methods.stream()
                    .filter(methodNode -> methodNode.name.equals("<init>"))
                    .findFirst()
                    .ifPresent(methodNode -> customRootPageSideBar(methodNode, modify));
        }
        return modify.get();
    }

    private static void customRootPageSideBar(MethodNode methodNode, AtomicBoolean modify) {
        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
        for (AbstractInsnNode insn = null; iterator.hasNext(); insn = iterator.next()) {
            if (!(insn instanceof MethodInsnNode)) continue;
            MethodInsnNode methodInsnNode = (MethodInsnNode) insn;
            if (methodInsnNode.getOpcode() != Opcodes.INVOKEVIRTUAL
                    || !"org/jackhuang/hmcl/ui/construct/AdvancedListBox".equals(methodInsnNode.owner)
                    || !"add".equals(methodInsnNode.name)) {
                continue;
            }
            AbstractInsnNode next = methodInsnNode.getNext();
            if (next.getOpcode() != Opcodes.ASTORE) continue;
            VarInsnNode sideBarASTORE = (VarInsnNode) next;
            iterator.next();
            iterator.add(new VarInsnNode(Opcodes.ALOAD, sideBarASTORE.var));
            iterator.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/ixnah/mpzt/MpztPlugin", "onSideBarInit", "(Lorg/jackhuang/hmcl/ui/construct/AdvancedListBox;)V", false));
            modify.set(true);
            return;
        }
    }
}
