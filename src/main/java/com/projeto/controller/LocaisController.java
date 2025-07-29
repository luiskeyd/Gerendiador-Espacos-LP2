// package com.projeto.controller;

// import com.projeto.model.Usuario;

// public class LocaisController {
//     public boolean adicionarLocal(String nome, String email, String senha, String tipo) {
//         if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
//             return false;
//         }

//         try {
//             // Verificar se usuário já existe
//             Usuario usuarioExistente = model.buscar(email);
//             if (usuarioExistente != null) {
//                 return false; // Usuário já existe
//             }

//             Usuario novoUsuario = new Usuario(nome, email, tipo, senha);
//             model.adicionar(novoUsuario);
//             return true;
//         } catch (Exception e) {
//             System.err.println("Erro no processo de cadastro: " + e.getMessage());
//             e.printStackTrace();
//             return false;
//         }
//     }
// }
