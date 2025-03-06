package cn.polister.infosys;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InfoSysApplicationTests {


    @Autowired
    private ChatModel chatModel;

    @Test
    void contextLoads() {
        System.out.println(chatModel.call("你好"));
    }

}
