class Solution:
    def eraseOverlapIntervals(self, intervals: List[List[int]]) -> int:
        # 1. Төгсгөлийн цэгээр нь эрэмбэлнэ
        intervals.sort(key=lambda x: x[1])

        removed = 0
        prevEnd = float('-inf')

        for start, end in intervals:
            if start >= prevEnd:
                # Давхцахгүй → үлдээнэ
                prevEnd = end
            else:
                # Давхцаж байна → энэ интервалыг устгав гэж тооцно
                removed += 1

        return removed

# Алгоритм (алхам алхмаар)
# intervals-ийг төгсгөлийн цэгээр нь (end) өсөхөөр эрэмбэлнэ.
# prevEnd = өмнө сонгосон интервалын төгсгөлийн цэг (эхэндээ −∞).
# Эрэмбэлсэн интервал бүр дээр:
#   Хэрэв start >= prevEnd бол давхцахгүй → энэ интервалыг үлдээнэ
#   → prevEnd = end
#   Хэрэв start < prevEnd бол өмнөхтэй давхцаж байна →
#   → энэ интервалыг устгасан гэж тооцоод removed++ (эрэмбэлсэн байгаа тул их дараа дуусдагийг устгасан гэж үзэж болно).
# Эцэст нь removed бол хариу.