for _ in range(int(input())):
    n=int(input());a=list(map(int,input().split()))
    d=[0]*n
    def f(l,r,dep):
        if l>r:return
        m=max(range(l,r+1),key=a.__getitem__)
        d[m]=dep
        f(l,m-1,dep+1)
        f(m+1,r,dep+1)
    f(0,n-1,0)
    print(*d)
    
