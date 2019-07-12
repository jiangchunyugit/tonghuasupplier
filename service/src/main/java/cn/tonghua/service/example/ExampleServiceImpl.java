package cn.tonghua.service.example;

import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl implements ExampleService{

    @Override
    public String testFirst() {
        return "welcome";
    }
}
