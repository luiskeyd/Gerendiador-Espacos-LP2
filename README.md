# Sistema de Gerenciamento de Espaços

Sistema para gerenciar reservas de espaços acadêmicos desenvolvido em Java com padrão MVC.

## 🏗️ Tipos de Espaços

- **Salas de Aula**
- **Laboratórios**
- **Salas de Reunião**
- **Quadras/Campos Esportivos**
- **Auditórios**

## ⚡ Funcionalidades

- ✅ Gerenciamento de usuários
- ✅ Cadastro de espaços
- ✅ Sistema de reservas
- ✅ Relatórios e estatísticas (semanal, mensal, anual)
- ✅ Logs de ações em .txt
- ✅ Emissão de relatórios em .csv

## 🛠️ Tecnologias

- **Java 11+**
- **Maven**
- **MySQL**
- **JDBC**
- **Padrão MVC**

## 📦 Estrutura do Projeto

```
src/main/java/com/example/
├── config/          # Configurações
├── model/           # Entidades
├── dao/             # Acesso a dados
├── service/         # Regras de negócio
├── controller/      # Controladores
├── view/            # Interface do usuário
└── util/            # Utilitários
```

## 🚀 Como Executar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/gerenciador-espacosjava.git
   ```

2. **Configure o banco de dados**
   - Instale o MySQL
   - Execute o script `src/main/resources/database.sql`
   - Configure as credenciais em `src/main/resources/application.properties`

3. **Compile e execute**
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.example.Main"
   ```

## 📊 Relatórios Gerados

- `relatorios/uso_semanal.csv`
- `relatorios/uso_mensal.csv`
- `relatorios/uso_anual.csv`

## 📝 Logs

- `logs/sistema.log` - Logs gerais do sistema
- `logs/acoes_usuarios.log` - Ações dos usuários

## 👥 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.
