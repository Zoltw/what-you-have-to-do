package efs.task.todoapp.service.handlers.user;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.Headers;
import efs.task.todoapp.IncorrectData;
import efs.task.todoapp.service.UnathorizedAccessException;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class UserEntityConverter {

    UserEntityConverter userEntityConverter;
    public UserEntityConverter(UserEntityConverter userEntityConverter) {
        this.userEntityConverter = userEntityConverter;
    }

    public static Map<String, String> convertToEntity(String headers) throws UnathorizedAccessException {

        try {
            if (headers == null) { throw new Exception();}
            Type map = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> jsonData = new Gson().fromJson(headers, map);
            if (jsonData == null || jsonData.size() <= 0) {
                throw new Exception();
            }
            return jsonData;
        } catch (Exception e) {
            throw new UnathorizedAccessException();
        }

    }

    public static String[] checkAuth(Headers headers) throws IncorrectData {

        String auth;
        try {
            if (headers == null) {
                throw new Exception();
            }
            List<String> headerList = headers.get("auth");
            if (headerList == null || headerList.size() < 1) {
                throw new Exception();
            }
            auth = headerList.get(0);
            if (null == auth || auth.isBlank()) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            throw new IncorrectData();
        }
        return getUserAuth(auth);
    }

    public static String[] getUserAuth(String auth) throws IncorrectData{
        String[] splitAuth = auth.split(":");

        if (splitAuth.length != 2){
            throw new IncorrectData();
        }

        String[] result = new String[2];

        for (int i = 0; i < 2; i++){
            try {
                if (splitAuth[i] == null || splitAuth[i].isBlank()){
                    throw new Exception();
                }
                result[i] = decode(splitAuth[i]);
            }
            catch (Exception e) {
                throw new IncorrectData();
            }
        }
        return result;
    }

    public static String decode(final String valueInBase64) {
        final Base64.Decoder base64Decoder = Base64.getDecoder();
        final byte[] decodedValueBytes = base64Decoder.decode(valueInBase64);

        return new String(decodedValueBytes, StandardCharsets.UTF_8);
    }


}
