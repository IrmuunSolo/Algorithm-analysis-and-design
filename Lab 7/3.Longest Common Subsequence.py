class Solution:
    def longestCommonSubsequence(self,a,b):
        dp=[0] * (len(b) + 1)
        for x in a:
            pre = 0
            for j,y in enumerate(b, 1):
                pre, dp[j] = dp[j], pre + 1 if x==y else max(dp[j], dp[j-1])
        return dp[-1]

    # def longestCommonSubsequence(self,a,b):
    #     # dp нь хоёр дахь string (b)-ийн хэмжээгээр 1D динамик хүснэгт
    #     dp=[0]*(len(b)+1)

    #     # a-ийн бүх тэмдэгтийг дарааллаар гүйж шалгана
    #     for x in a:

    #         pre=0  # өмнөх мөрийн dp[j-1] утгыг хадгалах түр хувьсагч

    #         for j,y in enumerate(b,1):  # b-ийн тэмдэгтүүдийг индекс 1-ээс эхлүүлж гүйж байна
    #             tmp=dp[j]  # одоогийн dp[j] утгыг дараагийн давталтад хадгалах
    #             # хэрвээ a ба b-ийн одоогийн тэмдэгтүүд ижил бол урт +1
    #             # үгүй бол зүүн эсвэл дээд утгын ихийг авах

    #             dp[j]=pre+1 if x==y else max(dp[j],dp[j-1])

    #             pre=tmp  # дараагийн итерацад ашиглахын тулд хуучин dp[j] хадгална
    
    #     return dp[-1]  # хамгийн сүүлийн элемент нь LCS урт