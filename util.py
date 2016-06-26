import math

# all symbols in this module that do not begin with "_" are fair game

## Factorials

# we memoize factorials in _fact, and then use that list to index
# individual factorials

factorials = [ 1, 1 ]
def mkfact(n):
    while len(factorials) <= n+1:
        factorials.append(factorials[-1] * len(factorials))

def fact(n):
    """ n! """
    mkfact(n)
    return factorials[n]

def choose(n, k): 
    """ n! / (k!(n-k)!) """
    mkfact(n)
    return factorials[n] / (factorials[k] * factorials[n-k])

## Fibonacci

# similarly, we memoize the terms of the fibonacci sequence
fibonacci = [ 1, 1 ]
def mkfib(n):
    while len(fibonacci) <= n+1:
        fibonacci.append(fibonacci[-1] + fibonacci[-2])

def fib(n):
    """ n'th fibonacci term, with fib(0) = fib(1) = 1 """
    mkfib(n)
    return fibonacci[n]

## GCD

def gcd(a, b):
    if a > b: a,b = b,a
    while 1:
        if a == 0: return b
        a, b = b % a, a

def lcm(a, b):
    return a*b/gcd(a,b)

## Primes

# the plan here is to construct a fast generator for primes, and then
# incrementally advance that generator as larger and larger primes are
# required.

#primes[k] is the k'th prime
primes = [ ]

def _primefactory_inner():
    def pbase():
        # generate likely primes, automatically not including
        # multiples of 2, 3, or 5.  This cuts the options to
        # check down from 30 to 8 -- a savings of almost 75%
        b = 0
        while True:
            for o in [ 1, 7, 11, 13, 17, 19, 23, 29 ]:
                yield b + o
            b += 30

    # because we're excluding their multiples, we have to explicitly
    # include 2, 3, and 5.
    yield 2
    yield 3
    yield 5

    pb = pbase()
    pb.next() # skip 1
    for i in range(7): # and include the remaining seven primes
        yield pb.next()

    while True:
        n = pb.next()

        sqrtn = int(math.sqrt(n))+1
        for p in primes:
            if p > sqrtn:
                yield n
                break
            if n % p == 0:
                break
        else:
            yield n

# add each yielded value to primes
def _primefactory():
    for p in _primefactory_inner():
        primes.append(p)
        yield p

_pgen = _primefactory()

def mkprimes(n):
    """
    Ensure that all primes <= n are in PRIMES.
    """
    while 1:
        p = _pgen.next()
        if p > n: break

def nprimes(n):
    """
    Ensure that at least n primes are in PRIMES
    """
    while len(primes) < n:
        _pgen.next()

def is_prime(n):
    mkprimes(n)
    return n in primes

def primegen():
    """
    Generate an infinite sequence of primes
    """
    i = 0
    mkprimes(100)
    while True:
        if i >= len(primes):
            mkprimes(2*primes[-1])
        yield primes[i]
        i += 1
    # TODO: a generic restartable iterator could help!

## Factoring

def nfactors(t):
    """ return the number of factors of t """
    return len(factors(t))

# surprisingly, this "clever" method is much, much slower than
# the less clever method above.
def nfactors_slooow(t):
    mkprimes(t)
    pfact_counts = []

    for p in primes:
        if p >= t:
            if p == t: pfact_counts.append((1, p))
            break
        count = 0
        while t % p == 0:
            t /= p
            count += 1
        if count:
            pfact_counts.append((count, p))

    # we want to take the product of k+1 for each count k
    prod = 1
    for k,p in pfact_counts: prod *= k+1
    return prod

def factors(t):
    """ return al of the factors of t """
    factors = []
    f = 0
    for f in xrange(1, int(math.ceil(math.sqrt(t)))):
        div, mod = divmod(t, f)
        if mod == 0:
            factors.append(f)
            factors.append(div)
    # catch perfect squares
    if (f+1)*(f+1) == t:
        factors.append(f+1)

    return factors

def divisible_by_all(n, ds):
    """ True if n is divisible by all divisors in ds """
    for d in ds:
        if n % d != 0:
            return False
    return True

## List utils

def findmin(l, cmp=cmp):
    if len(l) == 0: return None
    if len(l) == 1: return l[0]
    if cmp(l[0], l[1]) < 0:
        min = l[0]
    else:
        min = l[1]
    for e in l[2:]:
        if cmp(min, e) > 0:
            min = e
    return min

## Palindromes
# (projecteuler *loves* palindrome!)

def is_palindrome(n):
    return str(n) == str(n)[::-1]
