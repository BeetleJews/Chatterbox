package letscode.Chatterbox.controller;


import com.sun.javafx.collections.MappingChange;
import letscode.Chatterbox.exceptions.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("message")  //обращения по message будут обрабатываться тут
public class MessageController {
    private int counter = 4; //счетчик(след. запись будет четвертая)

    private List<Map<String, String>> messages = new ArrayList<Map<String, String>>(){{
        add(new HashMap<String, String>() {{ put("id", "1"); put("text", "First message");}});
        add(new HashMap<String, String>() {{ put("id", "2"); put("text", "Second message");}});
        add(new HashMap<String, String>() {{ put("id", "3"); put("text", "Third message");}});
    }};

    @GetMapping
    public List<Map<String, String>> getOne() {
        return messages;
    }

    @GetMapping("{id}")   //по запросу message/{id} перехватывает
    public Map<String, String> getOne(@PathVariable String id){
        return getMessage(id);
    }

    private Map<String, String> getMessage(@PathVariable String id) {
        return messages.stream()
                .filter(message -> message.get("id").equals(id))  //фильтруем ненужное
                .findFirst()  // берем первое
                .orElseThrow(NotFoundException::new);    //если нет значений выдать 404
    }

    @PostMapping  //добавление элемента
    public Map<String, String> create(@RequestBody Map<String, String> message){
        message.put("id", String.valueOf(counter++));
        messages.add(message);

        return message;
    }

    @PutMapping("{id}") //обновление конкретной записи
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message){
        Map<String, String> messageFromDb = getMessage(id);  //получаем запись

        messageFromDb.putAll(message);  //обновляем поля полученные от пользователя
        messageFromDb.put("id", id); //изменение именно по тому id по которому был произведен запрос

        return  messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Map<String, String> message = getMessage(id);

        messages.remove(message);
    }

}
