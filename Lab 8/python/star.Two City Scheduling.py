class Solution:
    def twoCitySchedCost(self, costs: List[List[int]]) -> int:
        # costA - costB утгаар эрэмбэлнэ
        costs.sort(key=lambda x: x[0] - x[1])
        n = len(costs) // 2
        # Эхний n: A хот, дараагийн n: B хот
        return sum(c[0] for c in costs[:n]) + sum(c[1] for c in costs[n:])

    # 2n хүн байна.
    # Хүн бүрийг A хот эсвэл B хот руу явуулна.
    # costs[i] = [costA, costB] — i-дэх хүнийг A хот руу явуулах үнэ, B хот руу явуулах үнэ.
    # Яг n хүнийг A хот, n хүнийг B хот руу явуулах ёстой.
    # Нийт зардлыг хамгийн бага болго.