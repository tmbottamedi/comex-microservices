package br.com.alura.comex.categoria;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastra(
            @RequestBody @Valid RequestCategoria request, BindingResult result) {

        if (result.hasErrors()) {
            String mensagem = result.getFieldError("nome").getDefaultMessage();
            return new ResponseEntity<>(mensagem, HttpStatus.BAD_REQUEST);
        }

        Categoria categoria = request.toCategoria();
        categoriaService.cadastrar(categoria);

        KafkaProducer kafkaProducer = new KafkaProducer<String, String>(properties());

        ProducerRecord producerRecord = new ProducerRecord<>("PRODUTO_CATEGORIA_CRIADA", categoria.getNome());

        try {
            kafkaProducer.send(producerRecord, (data, ex) -> {
                if (ex != null) {
                    System.out.println("Erro ao enviar mensagem para o Kafka.");
                    ex.printStackTrace();
                    return;
                }

                System.out.println("Mensagem enviada para topic=" + data.topic() + ", key=" + categoria.getNome());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(categoria, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> lista() {
        List<Categoria> categorias = categoriaService.listar();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }
}