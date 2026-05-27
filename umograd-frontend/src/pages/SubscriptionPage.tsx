import { useEffect, useState } from "react";
import Navbar from "../components/Navbar.tsx";
import Footer from "../components/Footer.tsx";
import Loader from "../components/Loader.tsx";
import Alert from "../components/Alert.tsx";
import "../components/Layout.css";

export default function SubscriptionPage() {
    const [loading, setLoading] = useState(false);
    const [hasSubscription, setHasSubscription] = useState(false);
    const [success, setSuccess] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isFormOpen, setIsFormOpen] = useState(false);

    const [cardNumber, setCardNumber] = useState("");
    const [expiry, setExpiry] = useState("");
    const [cvc, setCvc] = useState("");

    useEffect(() => {
        checkSubscriptionStatus();
    }, []);

    const checkSubscriptionStatus = async () => {
        try {
            setLoading(true);
            const token = localStorage.getItem("accessToken");
            const res = await fetch("http://localhost:8080/api/v1/subscription/check-sub", {
                headers: { Authorization: `Bearer ${token}` }
            });
            if (res.ok) {
                const data = await res.json();
                setHasSubscription(!!data.hasActiveSubscription);
            }
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const formatCardNumber = (value: string) => {
        const v = value.replace(/\D/g, "").substring(0, 16);
        const parts = v.match(/.{1,4}/g);
        return parts ? parts.join(" ") : v;
    };

    const formatExpiry = (value: string) => {
        const v = value.replace(/\s+/g, "").replace(/[^0-9]/gi, "");
        if (v.length >= 2) {
            return `${v.substring(0, 2)}/${v.substring(2, 4)}`;
        }
        return v;
    };

    const handlePaymentSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            setError(null);
            const token = localStorage.getItem("accessToken");
            const res = await fetch("http://localhost:8080/api/v1/subscription/process-payment", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({
                    cardNumber: cardNumber.replace(/\s/g, ""),
                    expiry,
                    cvc
                })
            });
            const data = await res.json();
            if (res.ok) {
                setSuccess(data.message);
                setHasSubscription(true);
                setIsFormOpen(false);
            } else {
                setError(data.error || "Ошибка авторизации транзакции.");
            }
        } catch {
            setError("Не удалось связаться с платежным шлюзом.");
        } finally {
            setLoading(false);
        }
    };
    return (
        <div className="app-layout">
            <Navbar />
            <main className="app-main" style={{ display: "flex", flexDirection: "column", alignItems: "center", padding: "40px 20px", fontFamily: "Nunito, sans-serif" }}>
                <div style={{ width: "100%", maxWidth: "480px", background: "white", borderRadius: "24px", padding: "35px", textAlign: "center", boxShadow: "0 10px 30px rgba(104, 158, 202, 0.12)" }}>
                    <h2 style={{ fontSize: "24px", fontWeight: 800, color: "#2d3748", margin: "0 0 8px 0" }}>Умоград Премиум</h2>
                    <p style={{ color: "#718096", fontSize: "14px", margin: "0 0 25px 0" }}>Локальный шлюз безопасных платежей образовательной платформы</p>

                    {success && <Alert type="success" message={success} />}
                    {error && <Alert type="error" message={error} />}

                    {hasSubscription ? (
                        <div style={{ border: "2px solid #7FCA68", borderRadius: "20px", padding: "30px", background: "rgba(127, 202, 104, 0.03)", marginTop: "10px" }}>
                            <div style={{ fontSize: "50px", marginBottom: "10px" }}>🎉</div>
                            <h3 style={{ fontSize: "20px", fontWeight: 800, color: "#2d3748", margin: "0 0 10px 0" }}>Подписка активна</h3>
                            <p style={{ color: "#718096", fontSize: "14px", margin: 0, lineHeight: "1.5" }}>
                                Поздравляем! У вас уже оформлена годовая подписка Умоград Премиум. Вам открыт неограниченный доступ ко всем тестам, Олимпиадам и расширенной ИИ-аналитике успеваемости.
                            </p>
                        </div>
                    ) : (
                        !isFormOpen ? (
                            <div style={{ border: "2px solid #689ECA", borderRadius: "20px", padding: "25px", background: "rgba(104, 158, 202, 0.01)" }}>
                                <span style={{ background: "#689ECA", color: "white", padding: "4px 12px", borderRadius: "20px", fontSize: "11px", fontWeight: 700, textTransform: "uppercase" }}>Выбор тарифа</span>
                                <h3 style={{ fontSize: "22px", fontWeight: 800, margin: "15px 0 5px 0", color: "#2d3748" }}>Полный доступ на год</h3>
                                <div style={{ fontSize: "32px", fontWeight: 800, color: "#689ECA", margin: "10px 0" }}>2 990 ₽ <span style={{ fontSize: "14px", color: "#718096", fontWeight: 500 }}>/ год</span></div>
                                <ul style={{ textAlign: "left", color: "#4a5568", fontSize: "14px", paddingLeft: "20px", margin: "20px 0", lineHeight: "1.6" }}>
                                    <li>🚀 Открытие скрытых Олимпиад и тестов высокой сложности</li>
                                    <li>📈 Еженедельные расширенные PDF-отчеты успеваемости</li>
                                    <li>🤖 Приоритетный ИИ-подбор гибридной образовательной траектории</li>
                                </ul>
                                <button onClick={() => setIsFormOpen(true)} style={{ width: "100%", background: "linear-gradient(90deg, #689ECA 0%, #8FDADB 100%)", border: "none", borderRadius: "30px", padding: "12px", color: "white", fontWeight: 700, fontSize: "15px", cursor: "pointer", boxShadow: "0 4px 15px rgba(104, 158, 202, 0.2)" }}>
                                    Подключить Премиум-доступ
                                </button>
                            </div>
                        ) : (
                            <form onSubmit={handlePaymentSubmit} style={{ background: "#f8fafc", padding: "25px", borderRadius: "20px", border: "1px solid rgba(104, 158, 202, 0.2)", textAlign: "left", display: "flex", flexDirection: "column", gap: "15px" }}>
                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", borderBottom: "1px solid #e2e8f0", paddingBottom: "10px", marginBottom: "5px" }}>
                                    <span style={{ fontSize: "13px", fontWeight: 700, color: "#4a5568" }}>💳 БЕЗОПАСНАЯ ОПЛАТА КАРТОЙ</span>
                                    <span style={{ fontSize: "11px", fontWeight: 700, color: "#a0aec0", letterSpacing: "1px" }}>МИР / VISA / MC</span>
                                </div>

                                <label style={{ display: "flex", flexDirection: "column", gap: "5px", fontSize: "13px", fontWeight: 700, color: "#4A5568" }}>
                                    Номер банковской карты
                                    <input type="text" placeholder="0000 0000 0000 0000" required value={cardNumber} maxLength={19} onChange={(e) => setCardNumber(formatCardNumber(e.target.value))} style={{ padding: "12px", borderRadius: "12px", border: "1px solid rgba(111,115,118,0.25)", fontSize: "14px", fontFamily: "Nunito", outline: "none", background: "white" }} />
                                </label>

                                <div style={{ display: "flex", gap: "15px" }}>
                                    <label style={{ flex: 1, display: "flex", flexDirection: "column", gap: "5px", fontSize: "13px", fontWeight: 700, color: "#4A5568" }}>
                                        Срок действия
                                        <input type="text" placeholder="ММ/ГГ" required value={expiry} maxLength={5} onChange={(e) => setExpiry(formatExpiry(e.target.value))} style={{ padding: "12px", borderRadius: "12px", border: "1px solid rgba(111,115,118,0.25)", fontSize: "14px", fontFamily: "Nunito", textAlign: "center", outline: "none", background: "white" }} />
                                    </label>
                                    <label style={{ flex: 1, display: "flex", flexDirection: "column", gap: "5px", fontSize: "13px", fontWeight: 700, color: "#4A5568" }}>
                                        Код CVC / CVV
                                        <input type="password" placeholder="***" required value={cvc} maxLength={3} onChange={(e) => setCvc(e.target.value.replace(/[^0-9]/g, ""))} style={{ padding: "12px", borderRadius: "12px", border: "1px solid rgba(111,115,118,0.25)", fontSize: "14px", fontFamily: "Nunito", textAlign: "center", outline: "none", background: "white" }} />
                                    </label>
                                </div>

                                <div style={{ display: "flex", gap: "10px", marginTop: "10px" }}>
                                    <button type="button" onClick={() => setIsFormOpen(false)} style={{ flex: 1, background: "#edf2f7", border: "none", borderRadius: "30px", padding: "12px", color: "#4a5568", fontWeight: 700, cursor: "pointer", fontFamily: "Nunito" }}>Отмена</button>
                                    <button type="submit" disabled={loading} style={{ flex: 2, background: "linear-gradient(90deg, #7FCA68 0%, #A3DB8F 100%)", border: "none", borderRadius: "30px", padding: "12px", color: "white", fontWeight: 700, cursor: "pointer", boxShadow: "0 4px 10px rgba(127, 202, 104, 0.2)", fontFamily: "Nunito" }}>
                                        Оплатить 2 990 ₽
                                    </button>
                                </div>
                            </form>
                        )
                    )}
                </div>
                {loading && <Loader />}
            </main>
            <Footer />
        </div>
    );
}
