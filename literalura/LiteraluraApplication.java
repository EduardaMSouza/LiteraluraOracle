package com.eduarda.literalura;

import com.eduarda.literalura.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Autowired
	private BookService bookService;

	@Override
	public void run(String... args) {
        try {
            bookService.initBooks();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

		int opcao;
		Scanner scanner = new Scanner(System.in);

		do {

			System.out.println("\n1 - BUSCAR LIVROS DISPONIVEIS" +
					"\n2 - BUSCAR LIVRO POR TITULO" +
					"\n3 - BUSCAR AUTOR POR NOME" +
					"\n4 - BUSCAR AUTOR VIVO EM DETERMINADO ANO" +
					"\n5 - BUSCAR LIVRO POR LINGUA" +
					"\n6 - SAIR");

			opcao = scanner.nextInt();
			scanner.nextLine();

			switch (opcao){
				case 1:

					bookService.getAllBooks();

					break;
				case 2:
					System.out.println("Digite o nome do livro que quer procurar");

					String name = scanner.nextLine().toLowerCase();

					bookService.getByName(name);
					break;

				case 3:
					System.out.println("Digite o nome do autor que quer procurar");
					String nameAuthor = scanner.nextLine().toLowerCase();

					bookService.getAuthorByName(nameAuthor);
					break;
				case 4:
					System.out.println("Digite o ano para pesquisa");

					String ano = scanner.nextLine();

					bookService.getAuthorsAliveInYear(ano);
					break;
				case 5:
					System.out.println("Digite a linguagem de sua preferencia");
					String language = scanner.nextLine();

					bookService.getBooksByLanguage(language);
					break;
				default:
					System.out.println("Opção invalida!");
					break;
			}

		} while (opcao != 6);

	}

}
