import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //Наша ссылка, на которую будем отправлять запрос
        String url = "https://api.nasa.gov/planetary/apod" +
                "?api_key=tfjNXrDzLRg4vRcNB0JLWF9xEG1aYVbkJp4SSJ58" +
                "&date=2024-04-16";
        //HTTP клиент, который будет отправлять запросы
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //Сущность, которая будет преобразовывать ответ в наш объект NASA
        ObjectMapper mapper = new ObjectMapper();

        // Отправляем запрос и получаем ответ с нашей картинкой
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        NASAAnswer answer = mapper.readValue(response.getEntity().getContent(), NASAAnswer.class);

        //Формируем автоматически название для файла
        String[] urlSeparated = answer.getUrl().split("/");
        String filename = urlSeparated[urlSeparated.length - 1];


        HttpGet httpGetImage = new HttpGet(answer.getUrl());
        CloseableHttpResponse image = httpClient.execute(httpGetImage);

        //сохраняем в файл
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        image.getEntity().writeTo(fileOutputStream);

    }
}

