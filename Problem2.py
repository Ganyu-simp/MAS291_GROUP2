import itertools
import random

#Q2.1
def is_derangement(perm):
    return all(i + 1 != val for i, val in enumerate(perm))

def ProbDerangement(n, method="exact", trials=100000):
    if method == "exact":
        perms = list(itertools.permutations(range(1, n + 1)))
        derangements = [p for p in perms if is_derangement(p)]
        return len(derangements) / len(perms)
    
    elif method == "approx":
        derangement_count = 0
        for _ in range(trials):
            perm = random.sample(range(1, n + 1), n)
            if is_derangement(perm):
                derangement_count += 1
        return derangement_count / trials

    else:
        raise ValueError("method must be 'exact' or 'approx'")
print(ProbDerangement(3, method="exact"))   # → 0.333...
print(ProbDerangement(10, method="approx")) # → approximately 1/e ≈ 0.367...

#Q2.2
def hill_distance(a, b):
    if a < b:
        return 2 * (b - a)
    elif a > b:
        return a - b
    else:
        return 0

def total_hill_distance(A):
    return sum(hill_distance(A[i], A[i+1]) for i in range(len(A) - 1))

def compute_expected_and_variance(n, trials=100000):
    # Exact expected value from formula
    expected = ((n - 1) * (n + 1)) / 2

    # Approximate variance using random permutations
    distances = []
    for _ in range(trials):
        perm = random.sample(range(1, n + 1), n)
        dist = total_hill_distance(perm)
        distances.append(dist)
    
    approx_variance = statistics.variance(distances)

    return expected, approx_variance
n = 5
expected, variance = compute_expected_and_variance(n)
print(f"E(X) = {expected}, V(X) ≈ {variance}")

#Q2.3
def length_of_LIS(arr):
    lis = []
    for x in arr:
        pos = bisect.bisect_left(lis, x)
        if pos == len(lis):
            lis.append(x)
        else:
            lis[pos] = x
    return len(lis)
#b
import random
import statistics
import math

def expected_LIS(n, trials=10000):
    lengths = []
    for _ in range(trials):
        perm = random.sample(range(1, n + 1), n)
        lis_len = length_of_LIS(perm)
        lengths.append(lis_len)
    mean = statistics.mean(lengths)
    return mean
n = 100
approx_E_X = expected_LIS(n)
print(f"Approximate E(X) for LIS of random permutation of 1..{n} is {approx_E_X}")
print(f"2 * sqrt({n}) ≈ {2 * math.sqrt(n)}")
