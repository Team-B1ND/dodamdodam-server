package b1nd.dodam.restapi.support.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
@JsonDeserialize(using = ResponseData.ResponseDataDeserializer.class)
public class ResponseData<T> extends Response {

    private final T data;

    private ResponseData(HttpStatus status, String message, T data) {
        super(status.value(), message);
        this.data = data;
    }

    public static <T> ResponseData<T> of(HttpStatus status, String message, T data) {
        return new ResponseData<>(status, message, data);
    }

    public static <T> ResponseData<T> ok(String message, T data) {
        return new ResponseData<>(HttpStatus.OK, message, data);
    }

    public static <T> ResponseData<T> created(String message, T data) {
        return new ResponseData<>(HttpStatus.CREATED, message, data);
    }

    public static class ResponseDataDeserializer extends JsonDeserializer<ResponseData> {
        @Override
        public ResponseData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            HttpStatus status = HttpStatus.valueOf(node.get("status").asInt());
            String message = node.get("message").asText();
            JsonNode dataNode = node.get("data");
            Object data = p.getCodec().treeToValue(dataNode, Object.class);
            return new ResponseData<>(status, message, data);
        }
    }

}

