from typing import List
from time import perf_counter
import tracemalloc
from text_justify import greedy_justify, dp_justify


def tokenize(text: str) -> List[str]:
    return [w for w in text.split() if w]


def prompt_int(msg: str) -> int:
    while True:
        try:
            v = int(input(msg).strip())
            if v > 0:
                return v
        except ValueError:
            pass
        print("Буруу утга. Дахин оруулна уу.")


def prompt_algo() -> str:
    print("Алгоритмаа сонгоно уу: DP -> 1, greedy -> 2")
    while True:
        s = input("> ").strip()
        if s == "1":
            return "dp"
        if s == "2":
            return "greedy"
        print("Зөвхөн 1 эсвэл 2 оруулна уу.")


def prompt_text() -> str:
    print("Текстээ оруулна уу (хоосон мөрөөр дуусгана):")
    lines = []
    while True:
        try:
            ln = input()
        except EOFError:
            break
        if ln.strip() == "":
            break
        lines.append(ln)
    return "\n".join(lines)


def main():
    width = prompt_int("Мөрийн өргөн: ")
    algo = prompt_algo()
    text = prompt_text()
    words = tokenize(text)
    if not words:
        print("Оруулах текст алга.")
        return

    # Measure time + peak memory during justification
    tracemalloc.start()
    t0 = perf_counter()
    if algo == "greedy":
        lines = greedy_justify(words, width)
    else:
        lines = dp_justify(words, width)
    t1 = perf_counter()
    current, peak = tracemalloc.get_traced_memory()
    tracemalloc.stop()

    print("Үр дүн:")
    for ln in lines:
        print(ln)

    # Report performance after output
    ms = (t1 - t0) * 1000.0
    print(f"\nГүйцэтгэлийн хугацаа: {ms:.3f} ms")
    print(f"Санах ойн зарцуулалт: {peak/1024:.1f} KiB")


if __name__ == "__main__":
    main()
