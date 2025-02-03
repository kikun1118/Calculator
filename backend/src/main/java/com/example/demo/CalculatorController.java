package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@CrossOrigin(origins = "*") // すべてのオリジンを許可
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final List<String> history = new LinkedList<>();

    @GetMapping("/calculate")
    public ResponseEntity<String> calculate(@RequestParam double num1, 
                                            @RequestParam String op, 
                                            @RequestParam double num2) {
        double result;

        // URLデコードして `+` や `*` などの特殊文字を正しく処理
        op = URLDecoder.decode(op, StandardCharsets.UTF_8);

        System.out.println("受け取った演算子: " + op);
        try {
            switch (op) {
                case "+":
                    result = num1 + num2;
                    break;
                case "-":
                    result = num1 - num2;
                    break;
                case "*":
                    result = num1 * num2;
                    break;
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
        synchronized (history) {
            if (history.size() >= 10) { // 履歴を最大10件に制限
                history.remove(0);
            }
            history.add(entry);
        }

        return ResponseEntity.ok(String.valueOf(result));
    }

    @GetMapping("/history")
    public ResponseEntity<List<String>> getHistory() {
        return ResponseEntity.ok(history);
    }
}