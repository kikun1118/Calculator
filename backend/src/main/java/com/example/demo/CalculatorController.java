package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Reactと接続
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final List<String> history = new ArrayList<>();

    @GetMapping("/calculate")
    public ResponseEntity<String> calculate(@RequestParam double num1, 
                                            @RequestParam String op, 
                                            @RequestParam double num2) {

                                                
        double result;

        // op = java.net.URLDecoder.decode(op, StandardCharsets.UTF_8);

        System.out.println("受け取った演算子: " + op);
        try {
            switch (op) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/":
                    if (num2 == 0) {
                        return ResponseEntity.badRequest().body("エラー: 0では割れません。");
                    }
                    result = num1 / num2;
                    break;
                default:
                    return ResponseEntity.badRequest().body("エラー: 無効な演算子です。+ - * / のみ使用できます。");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("サーバーエラーが発生しました。");
        }

        String entry = num1 + " " + op + " " + num2 + " = " + result;
        history.add(entry);
        return ResponseEntity.ok("計算結果: " + result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getHistory() {
        return ResponseEntity.ok(history);
    }
}
