$baseUrl = "http://localhost:8080/api"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Travel Recommendation Test" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1] Logging in..." -ForegroundColor Yellow

$body = @{
    username = "cc_z"
    password = "123456Ccz"
} | ConvertTo-Json

$loginResp = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $body -ContentType "application/json"

if ($loginResp.code -ne 200 -and $loginResp.code -ne 0) {
    Write-Host "Login failed: $($loginResp.msg)" -ForegroundColor Red
    exit
}

$token = $loginResp.data.token
Write-Host "    Token obtained: $($token.Substring(0,25))..." -ForegroundColor Green

$hdrs = @{
    Authorization = "Bearer $token"
}

Write-Host ""
Write-Host "[2] User cc_z browse history: 西湖(ID:12), 黄山(ID:13) - 类别: 自然风光" -ForegroundColor Yellow
Write-Host ""

Write-Host "[3] Algorithm 1: Based on User Preference" -ForegroundColor Yellow

$prefResp = Invoke-RestMethod -Uri "$baseUrl/preference/recommend" -Method Get -Headers $hdrs
if ($prefResp.code -eq 200 -or $prefResp.code -eq 0) {
    $prefList = $prefResp.data
    Write-Host "    Result count: $($prefList.Count)" -ForegroundColor Cyan
    if ($prefList.Count -eq 0) {
        Write-Host "    (User has NO explicit preferences set)" -ForegroundColor Gray
    }
} else {
    Write-Host "    Error: $($prefResp.msg)" -ForegroundColor Red
}

Write-Host ""

Write-Host "[4] Algorithm 2: Based on Browse History" -ForegroundColor Yellow
Write-Host "    (Auto-analyze from browse history: West Lake, Yellow Mountain -> Nature category)" -ForegroundColor Gray

Write-Host ""

Write-Host "[5] Hybrid Recommendation (1:1)" -ForegroundColor Yellow

$hybridResp = Invoke-RestMethod -Uri "$baseUrl/preference/recommend/hybrid" -Method Get -Headers $hdrs
if ($hybridResp.code -eq 200 -or $hybridResp.code -eq 0) {
    $hybridList = $hybridResp.data
    Write-Host "    Total: $($hybridList.Count)" -ForegroundColor Cyan

    Write-Host ""
    Write-Host "    --- Result Details ---" -ForegroundColor Magenta

    for ($i = 0; $i -lt $hybridList.Count; $i++) {
        $scenic = $hybridList[$i]
        $src = if (($i + 1) % 2 -eq 1) { "UserPreference" } else { "BrowseHistory" }
        $name = $scenic.name
        $cat = $scenic.categoryName
        $score = $scenic.score
        $views = $scenic.viewCount
        Write-Host "    $($i+1). $name | Category: $cat | Score: $score | Views: $views" -ForegroundColor White
        Write-Host "       Source: $src" -ForegroundColor Gray
    }
} else {
    Write-Host "    Error: $($hybridResp.msg)" -ForegroundColor Red
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Test Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
