var MethodNode = Java.type("org.objectweb.asm.tree.MethodNode");
var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
var AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
var Opcodes = Java.type("org.objectweb.asm.Opcodes");

function initializeCoreMod() {
    return {
        "client_mc": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.client.Minecraft",
                "methodName": "<init>",
                "methodDesc": "(Lnet/minecraft/client/main/GameConfig;)V"
            },
            "transformer": function(n) {
                // n.instructions.insert(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "", "java.lang.System.exit", "(I)V"));
                var it = n.instructions.iterator();
                while (it.hasNext()) {
                    var i = it.next();

                    switch (i.getType()) {
                        case AbstractInsnNode.TYPE_INSN:
                            switch(i.getOpcode()) {
                                case Opcodes.NEW:
                                    switch (i.desc) {
                                        case "com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService":
                                            i.desc = "me/xwashere/uauth/auth/uauth_authentication_service";
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case AbstractInsnNode.METHOD_INSN:
                            switch (i.getOpcode()) {
                                case Opcodes.INVOKESPECIAL:
                                    switch (i.owner) {
                                        case "com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService":
                                            i.owner = "me/xwashere/uauth/auth/uauth_authentication_service";
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                }

                return n;
            }
        },
        "server_main": {
            "target": {
                "type": "METHOD",
                "class": "net.minecraft.server.Main",
                "methodName": "main",
                "methodDesc": "([Ljava/lang/String;)V"
            },
            "transformer": function(n) {
                var it = n.instructions.iterator();
                while (it.hasNext()) {
                    var i = it.next();

                    switch (i.getType()) {
                        case AbstractInsnNode.TYPE_INSN:
                            switch(i.getOpcode()) {
                                case Opcodes.NEW:
                                    switch (i.desc) {
                                        case "com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService":
                                            i.desc = "me/xwashere/uauth/auth/uauth_authentication_service";
                                            break;
                                    }
                                    break;
                            }
                            break;
                        case AbstractInsnNode.METHOD_INSN:
                            switch (i.getOpcode()) {
                                case Opcodes.INVOKESPECIAL:
                                    switch (i.owner) {
                                        case "com/mojang/authlib/yggdrasil/YggdrasilAuthenticationService":
                                            i.owner = "me/xwashere/uauth/auth/uauth_authentication_service";
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                }
            }
        }
    }
}