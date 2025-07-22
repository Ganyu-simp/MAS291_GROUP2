import math
import random


#Q3.1
def firstContact(a, b, x):
    # Normalize angle x to [0, 2π]
    x = x % (2 * math.pi)

    # Direction vector (dx, dy)
    dx = math.cos(x)
    dy = math.sin(x)

    # Find time t until the ball hits any of the four walls
    tx = float('inf')
    ty = float('inf')

    # Check vertical walls at x = 0 or x = 1
    if dx > 0:
        tx = (1 - a) / dx
    elif dx < 0:
        tx = -a / dx

    # Check horizontal walls at y = 0 or y = 1
    if dy > 0:
        ty = (1 - b) / dy
    elif dy < 0:
        ty = -b / dy

    # Minimum time to first collision
    t = min(tx, ty)

    # Collision point
    c = a + t * dx
    d = b + t * dy

    # Determine which wall is hit and compute new direction
    if abs(t - tx) < 1e-9:
        # Vertical wall: reflect horizontally (reverse dx)
        y = math.atan2(dy, -dx)
    else:
        # Horizontal wall: reflect vertically (reverse dy)
        y = math.atan2(-dy, dx)

    # Normalize new angle to [0, 2π]
    y = y % (2 * math.pi)
    return (c, d, y)
result = firstContact(0.3, 0.4, math.pi / 3)
print(f"First contact point: ({result[0]:.3f}, {result[1]:.3f}), New angle: {result[2]:.3f} radians")

  #Q3.2
# Number of samples
N = 1000000
total = 0
count = 0

for _ in range(N):
    # a ∈ [0, 0.5]
    a = random.uniform(0, 0.5)
    # x ∈ [0, π]
    x = random.uniform(0, math.pi)
    
    # Skip if sin(x) is 0 to avoid division by zero
    sin_x = math.sin(x)
    if sin_x == 0:
        continue

    cos_x = math.cos(x)
    t = 1 / sin_x
    r = a + 2 * t * cos_x
    r_mod = r % 1  # Wrap into [0, 1]
    
    total += r_mod
    count += 1

# Estimate E(R)
E_R = total / count
print(f"Estimated E(R) = {E_R:.5f}")

