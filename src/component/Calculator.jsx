import { useState, useEffect } from "react";

export default function Calculator() {
    const [num1, setNum1] = useState("");
    const [num2, setNum2] = useState("");
    const [operator, setOperator] = useState("+");
    const [result, setResult] = useState("");
    const [history, setHistory] = useState([]);

    // 計算リクエストを送信
    const calculate = async () => {
        if (num1 === "" || num2 === "") {
            alert("数値を入力してください");
            return;
        }

        const requestUrl = `http://localhost:8080/api/calculate?num1=${num1}&op=${encodeURIComponent(operator)}&num2=${num2}`;
        console.log("リクエストURL:", requestUrl);
        console.log("演算子の値:", operator);

        try {
            const response = await fetch(requestUrl);
            const data = await response.text();
            setResult(data);
            fetchHistory();
        } catch (error) {
            console.error("エラー:", error);
        }
    };

    // 計算履歴を取得
    const fetchHistory = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/history");
            const data = await response.json();
            setHistory(data);
        } catch (error) {
            console.error("履歴取得エラー:", error);
        }
    };

    // 初回レンダリング時に履歴を取得
    useEffect(() => {
        fetchHistory();
    }, []);

    return (
        <div style={{ textAlign: "center", marginTop: "20px" }}>
            <h2>電卓アプリ</h2>

            <input
                type="number"
                value={num1}
                onChange={(e) => setNum1(e.target.value)}
                placeholder="数値1"
            />

            <select value={operator} onChange={(e) => setOperator(e.target.value)}>
                <option value="+">+</option>
                <option value="-">-</option>
                <option value="*">*</option>
                <option value="/">/</option>
            </select>

            <input
                type="number"
                value={num2}
                onChange={(e) => setNum2(e.target.value)}
                placeholder="数値2"
            />

            <button onClick={calculate}>計算</button>

            <h3>{result}</h3>

            <h3>計算履歴</h3>
            <ul>
                {history.map((entry, index) => (
                    <li key={index}>{entry}</li>
                ))}
            </ul>
        </div>
    );
}
