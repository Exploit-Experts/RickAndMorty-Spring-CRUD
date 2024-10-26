 <div align="center" text-align="center">
    <img src="https://capsule-render.vercel.app/api?type=waving&height=200&color=gradient&text=RickAndMorty%20API&reversal=false">
</div>

# 🚀Rick and Morty Spring API

RickAndMorty-Spring-API é um backend desenvolvido com Java e Spring Boot que implementa uma API RESTful para listar dados dos personagens da série Rick and Morty. O projeto permite a visualização de informações dos personagens e está preparado para ser consumido por um front-end separado. Este serviço fornece uma base robusta para integração com interfaces cliente que consumam dados de personagens por meio de endpoints.
</br>

## 🎯 Objetivo

Criar uma API RESTful que permita consumir e visualizar dados de personagens da série Rick and Morty, fornecendo
endpoints para serem utilizados no [front-end Angular](https://github.com/Exploit-Experts/RickAndMorthy-client).

</br>

## 🧑🏻‍💻Credits


||           |
| ---------------- | ---------------- |
| <img src="https://avatars.githubusercontent.com/u/114788642?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/brunoliratm'>Bruno Magno</a> |
| <img src="https://avatars.githubusercontent.com/u/127964717?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/Paulo-Araujo-Jr'>Paulo de Araujo</a> |
| <img src="https://avatars.githubusercontent.com/u/126338859?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/MrMesquita'>Marcelo Mesquita</a> |
| <img src="https://avatars.githubusercontent.com/u/126990110?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/Jonathanwsr'>Jonathan Rocha</a> |
| <img src="https://avatars.githubusercontent.com/u/180599406?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/Klismans-Nazario'>Klismans Nazário</a> |
| <img src="https://avatars.githubusercontent.com/u/126925371?v=4" float="left" width="40px" height=40px> | <a href='https://github.com/leandrouser'>Leandro Oliveira</a> |


</br>

---

## 🛠️ Tecnologias Utilizadas

- Java 21
- Spring Boot
- Maven
- MySQL 8.0.23

</br>

## 📂 Instalação e Execução

1. Clone o repositório:

```bash
git clone https://github.com/Exploit-Experts/RickAndMorty-Spring-API.git
```
2. Navegue até o diretório do projeto:

```bash
cd RickAndMorty-Spring-API
```
3. Copile project
```java
mvn clean install
```
4. Execute the jar
```
java -jar target/rickMorty-0.0.1-SNAPSHOT.jar
```

</br>

## 📃 Endpoints

- `GET /character/{id}` - Obtém um personagem específico pelo ID.
- `GET /episodes` - Obtém todos episódios.
- `GET /episodes/{id}` - Obtém um episódio especifico pelo ID.
- `GET /locations/` - Obtém todas localizações.
- `GET /locations/{id}` - Obtém uma localização específica pelo ID.


---

## 🤝 Contributing

<p>We welcome contributions from the open-source community. If you have any ideas, bug fixes, or feature requests, feel free to submit a pull request.</p>

</br>

## ⚖️ Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais informações.

<img src="https://capsule-render.vercel.app/api?type=waving&height=200&color=gradient&reversal=false&section=footer">
