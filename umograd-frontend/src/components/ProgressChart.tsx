type ProgressChartProps = {
    data: {
        progressPoints: { date: string; score: number }[];
        timePoints: { date: string; averageSeconds: number }[];
        difficultyStats: Record<string, number>;
    };
};

export default function ProgressChart({ data }: ProgressChartProps) {
    const progressData = data?.progressPoints || [];
    const timeData = data?.timePoints || [];
    const diffStats = data?.difficultyStats || { EASY: 0, MEDIUM: 0, HARD: 0 };

    const totalDiffTasks = (diffStats.EASY || 0) + (diffStats.MEDIUM || 0) + (diffStats.HARD || 0);

    return (
        <div style={{ display: "flex", flexDirection: "column", gap: "25px", fontFamily: "Nunito, sans-serif", width: "100%", boxSizing: "border-box" }}>

            <div style={{ background: "#f8fafc", padding: "20px", borderRadius: "24px", border: "1px solid rgba(104, 158, 202, 0.15)", boxSizing: "border-box", width: "100%" }}>
                <h4 style={{ margin: "0 0 15px 0", color: "#2d3748", fontSize: "16px", fontWeight: 800 }}>Успешность выполнения (%)</h4>
                {progressData.length === 0 ? (
                    <div style={{ textAlign: "center", padding: "20px", color: "#a0aec0", fontSize: "14px" }}>Нет данных за выбранный период</div>
                ) : (
                    <div style={{ width: "100%", height: "180px" }}>
                        <svg width="100%" height="100%" viewBox="0 0 600 180">
                            <line x1="60" y1="20" x2="60" y2="140" stroke="#e2e8f0" strokeWidth="1.5" />
                            <line x1="60" y1="140" x2="560" y2="140" stroke="#e2e8f0" strokeWidth="1.5" />
                            {[0, 25, 50, 75, 100].map((val) => {
                                const y = 140 - (val * 110) / 100;
                                return (
                                    <g key={val}>
                                        <text x="20" y={y + 4} fill="#718096" style={{ fontSize: "11px", fontWeight: 700 }}>{val}%</text>
                                        <line x1="55" y1={y} x2="560" y2={y} stroke="#f1f5f9" strokeWidth="1" strokeDasharray="4 4" />
                                    </g>
                                );
                            })}
                            {progressData.map((pt, idx) => {
                                const x = 80 + (idx * 450) / Math.max(1, progressData.length - 1);
                                const y = 140 - (pt.score * 110) / 100;
                                const nextPt = progressData[idx + 1];
                                const nextX = nextPt ? 80 + ((idx + 1) * 450) / Math.max(1, progressData.length - 1) : x;
                                const nextY = nextPt ? 140 - (nextPt.score * 110) / 100 : y;

                                return (
                                    <g key={idx}>
                                        {nextPt && <line x1={x} y1={y} x2={nextX} y2={nextY} stroke="#4A90E2" strokeWidth="3" strokeLinecap="round" />}
                                        <circle cx={x} cy={y} r="5" fill="#4A90E2" stroke="white" strokeWidth="2" />
                                        <text x={x} y="158" fill="#718096" style={{ fontSize: "11px", fontWeight: 700 }} textAnchor="middle">
                                            {pt.date.substring(5)}
                                        </text>
                                        <text x={x} y={y - 8} fill="#2d3748" style={{ fontSize: "11px", fontWeight: 800 }} textAnchor="middle">{pt.score}%</text>
                                    </g>
                                );
                            })}
                        </svg>
                    </div>
                )}
            </div>

            <div style={{ background: "#f8fafc", padding: "20px", borderRadius: "24px", border: "1px solid rgba(104, 158, 202, 0.15)", boxSizing: "border-box", width: "100%" }}>
                <h4 style={{ margin: "0 0 15px 0", color: "#2d3748", fontSize: "16px", fontWeight: 800 }}>Динамика времени решения (сек)</h4>
                {timeData.length === 0 ? (
                    <div style={{ textAlign: "center", padding: "20px", color: "#a0aec0", fontSize: "14px" }}>Нет данных за выбранный период</div>
                ) : (
                    <div style={{ width: "100%", height: "180px" }}>
                        <svg width="100%" height="100%" viewBox="0 0 600 180">
                            <line x1="60" y1="20" x2="60" y2="140" stroke="#e2e8f0" strokeWidth="1.5" />
                            <line x1="60" y1="140" x2="560" y2="140" stroke="#e2e8f0" strokeWidth="1.5" />

                            {timeData.map((pt, idx) => {
                                const maxSec = Math.max(...timeData.map(d => d.averageSeconds || 1), 10);
                                const x = 90 + (idx * 430) / Math.max(1, timeData.length);
                                const barHeight = ((pt.averageSeconds || 0) * 110) / maxSec;
                                const y = 140 - barHeight;

                                return (
                                    <g key={idx}>
                                        <rect x={x - 12} y={y} width="24" height={barHeight} fill="#689ECA" rx="5" style={{ WebkitPrintColorAdjust: "exact", printColorAdjust: "exact" }} />
                                        <text x={x} y="158" fill="#718096" style={{ fontSize: "11px", fontWeight: 700 }} textAnchor="middle">
                                            {pt.date.substring(5)}
                                        </text>
                                        <text x={x} y={y - 8} fill="#2d3748" style={{ fontSize: "11px", fontWeight: 800 }} textAnchor="middle">{pt.averageSeconds}с</text>
                                    </g>
                                );
                            })}
                        </svg>
                    </div>
                )}
            </div>

            <div style={{ background: "#f8fafc", padding: "20px", borderRadius: "24px", border: "1px solid rgba(104, 158, 202, 0.15)", boxSizing: "border-box", width: "100%" }}>
                <h4 style={{ margin: "0 0 15px 0", color: "#2d3748", fontSize: "16px", fontWeight: 800 }}>Соотношение решенных задач по сложностям</h4>
                {totalDiffTasks === 0 ? (
                    <div style={{ textAlign: "center", padding: "20px", color: "#a0aec0", fontSize: "14px" }}>Нет выполненных заданий</div>
                ) : (
                    <div style={{ display: "flex", flexDirection: "column", gap: "12px", padding: "5px 0" }}>
                        {[
                            { name: "Легкие (EASY)", count: diffStats.EASY || 0, color: "#7FCA68" },
                            { name: "Средние (MEDIUM)", count: diffStats.MEDIUM || 0, color: "#EC9F48" },
                            { name: "Сложные (HARD)", count: diffStats.HARD || 0, color: "#FA5252" }
                        ].map((item) => {
                            const pct = totalDiffTasks > 0 ? Math.round((item.count * 100) / totalDiffTasks) : 0;
                            return (
                                <div key={item.name} style={{ display: "flex", flexDirection: "column", gap: "5px" }}>
                                    <div style={{ display: "flex", justifyContent: "space-between", fontSize: "13px", fontWeight: 700, color: "#4a5568" }}>
                                        <span>{item.name}</span>
                                        <span>{item.count} шт. ({pct}%)</span>
                                    </div>
                                    <div style={{ width: "100%", height: "10px", background: "#edf2f7", borderRadius: "30px", overflow: "hidden", WebkitPrintColorAdjust: "exact", printColorAdjust: "exact" }}>
                                        <div style={{ width: `${pct}%`, height: "100%", backgroundColor: item.color, borderRadius: "30px", WebkitPrintColorAdjust: "exact", printColorAdjust: "exact" }}></div>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>

        </div>
    );
}
