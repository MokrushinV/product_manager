## Xsolla summer school 2021 test case

> #### Методы API для управления товарами — операции CRUD. Товар определяется уникальным идентификатором и обязательно должен иметь SKU, имя, тип, стоимость. Предполагается наличие следующих REST-методов:  
> - Создание товара. Метод генерирует и возвращает уникальный идентификатор товара.  
> - Редактирование товара. Метод изменяет все данные о товаре по его идентификатору или SKU.  
> - Удаление товара по его идентификатору или SKU.  
> - Получение информации о товаре по его идентификатору или SKU.  
> - Получение каталога товаров. Метод возвращает список всех добавленных товаров.  
> - Обратите внимание, что товаров может быть много. Необходимо предусмотреть возможность снизижения нагрузки на сервис. Вариант реализации: возвращайте список товаров по частям.  
> - Документацию в README. Обязательно укажите последовательность действий для запуска и локального тестирования API.  

> #### Дополнительная часть
> - Фильтрация товаров по их типу и/или стоимости в методе получения каталога товаров.
> - Спецификация OpenAPI 2.0 или 3.0 (бывший Swagger). Документация для разработанного REST API.
> - Dockerfile для создания образа приложения системы. Желательно наличие файла Docker-compose.
> - Модульные и функциональные тесты.
> - Развертывание приложения на любом публичном хостинге, например, на heroku.

tldr: all primary tasks have been done, but only the first of the additional part (the filtration one).
## About
REST API built with SPRING, Hibernate, HATEOAS, H2-in memory.
WebApp handles CRUD operations with preuploaded data.

# How to run project
In order to launch the web-app, regrettably (I have troubles with dockers ports), one need to download and install java (at least 15v):  
https://www.oracle.com/java/technologies/javase-downloads.html

and install STS  
https://spring.io/tools

# How to use
Web-app is accessible by browser: "http://localhost:8080/products?pageNo=0&pageSize=7&sortBy=id"

  where
  
        pageNo - number of page (default = 0),
        
        pageSize - number of products in one page (default = 7),
        
        sortBy - name of a column to sort (default = id);
        
          possible values: id, sku, name, productType, price;

//post a new product  
curl -X POST localhost:8080/products -H "Content-type:application/hal+json" -d "{""name"": ""Cool Game"", ""productType"": ""GAME"", ""price"": ""2000.00""}"

//trying to post already existed product - geting already existed product  
curl -X POST localhost:8080/products -v -H "Content-type:application/hal+json" -d "{""name"": ""Awesome Game"", ""productType"": ""GAME"", ""price"": ""1000.00""}"


//delete product by id  
curl -X DELETE localhost:8080/products/3

//delete product by sku  
curl -X DELETE localhost:8080/products/sku/3AWET-ST0P700

//update a product by id  
curl -X PUT localhost:8080/products/1 -H "Content-type:application/hal+json" -d "{""name"": ""Awesome Game"", ""productType"": ""GAME"", ""price"": ""2500.00""}"

//trying to update to a new product  - if it already exist, geting already existed product  
curl -X PUT localhost:8080/products/1 -v -H "Content-type:application/hal+json" -d "{""name"": ""Awesome Game 2"", ""productType"": ""GAME"", ""price"": ""1500.00""}"

//upadate a product by sku  
curl -X PUT localhost:8080/products/sku/0AWEGAMT1P1000 -H "Content-type:application/hal+json" -d "{""name"": ""Awesome Game"",
                                                                                                   ""productType"": ""GAME"",
                                                                                                   ""price"": ""2500.00""}"

//get a product by id  
curl -X GET localhost:8080/products/3

//get a product by sku  
curl -X GET localhost:8080/products/sku/3AWET-ST0P700
