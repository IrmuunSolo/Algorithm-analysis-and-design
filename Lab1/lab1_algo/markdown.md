# Random Number Generation in Python

Python provides several ways to generate random numbers. The most common method is using the `random` module.

```python
import random

rand_int = random.randint(1, 10)
print("Random integer:", rand_int)

rand_float = random.random()
print("Random float:", rand_float)
```

## Useful Functions

- `random.randint(a, b)`: Returns a random integer N such that `a <= N <= b`.
- `random.random()`: Returns a random float in the range `[0.0, 1.0)`.
- `random.choice(seq)`: Returns a random element from a non-empty sequence.
