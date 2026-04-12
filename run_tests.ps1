$baseUrl = "http://localhost:8080/api"
$testResults = @()
$testNumber = 0

function AddResult {
    param([string]$tc, [string]$status, [string]$expected, [string]$actual, [string]$notes = "")
    $script:testResults += [PSCustomObject]@{
        "TestCase" = $tc
        "Status" = $status
        "Expected" = $Expected
        "Actual" = $Actual
        "Notes" = $Notes
    }
}

function TestPost {
    param(
        [string]$Name,
        [string]$Url,
        [hashtable]$Headers = @{},
        [string]$Body,
        [string]$ExpectedCode,
        [string]$Notes = ""
    )
    $script:testNumber++
    Write-Host "`nTC-$testNumber : $Name" -ForegroundColor Cyan
    try {
        $params = @{ Uri = $Url; Method = "Post"; ContentType = "application/x-www-form-urlencoded"; Body = $Body }
        if ($Headers.Count -gt 0) { $params.Headers = $Headers }
        $response = Invoke-RestMethod @params
        $actualCode = $response.code
        $actualMsg = $response.message
        $pass = $false
        if ($ExpectedCode -eq "200" -and $actualCode -eq 200) { $pass = $true }
        if ($ExpectedCode -eq "500" -and $actualCode -eq 500) { $pass = $true }
        if ($pass) {
            Write-Host "[PASS] code=$actualCode msg=$actualMsg" -ForegroundColor Green
            AddResult -tc "TC-$testNumber : $Name" -status "PASS" -expected "$ExpectedCode" -actual "$actualCode/$actualMsg" -notes $Notes
        } else {
            Write-Host "[FAIL] code=$actualCode msg=$actualMsg" -ForegroundColor Red
            AddResult -tc "TC-$testNumber : $Name" -status "FAIL" -expected "$ExpectedCode" -actual "$actualCode/$actualMsg" -notes $Notes
        }
        return $response
    } catch {
        Write-Host "[FAIL-Exception] $($_.Exception.Message)" -ForegroundColor Red
        AddResult -tc "TC-$testNumber : $Name" -status "FAIL" -expected "$ExpectedCode" -actual "Exception" -notes $_.Exception.Message
        return $null
    }
}

function TestGet {
    param(
        [string]$Name,
        [string]$Url,
        [hashtable]$Headers = @{},
        [string]$ExpectedCode,
        [string]$Notes = ""
    )
    $script:testNumber++
    Write-Host "`nTC-$testNumber : $Name" -ForegroundColor Cyan
    try {
        $params = @{ Uri = $Url; Method = "Get" }
        if ($Headers.Count -gt 0) { $params.Headers = $Headers }
        $response = Invoke-RestMethod @params
        $actualCode = $response.code
        if ($ExpectedCode -eq "200" -and $actualCode -eq 200) {
            Write-Host "[PASS] code=$actualCode" -ForegroundColor Green
            AddResult -tc "TC-$testNumber : $Name" -status "PASS" -expected "$ExpectedCode" -actual "$actualCode" -notes $Notes
        } else {
            Write-Host "[FAIL] code=$actualCode" -ForegroundColor Red
            AddResult -tc "TC-$testNumber : $Name" -status "FAIL" -expected "$ExpectedCode" -actual "$actualCode" -notes $Notes
        }
        return $response
    } catch {
        Write-Host "[FAIL-Exception] $($_.Exception.Message)" -ForegroundColor Red
        AddResult -tc "TC-$testNumber : $Name" -status "FAIL" -expected "$ExpectedCode" -actual "Exception" -notes $_.Exception.Message
        return $null
    }
}

Write-Host "========================================" -ForegroundColor Yellow
Write-Host "Travel Platform - Automated Test Suite" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow

# ---- 1. Login (JSON body) ----
Write-Host "`n[1] Login" -ForegroundColor Magenta

$login1 = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body '{"username":"testuser1","password":"Test123456"}' -ContentType "application/json"
$token1 = $login1.data.token
$h1 = @{ Authorization = "Bearer $token1" }
Write-Host "User1 logged in, token: $($token1.Substring(0,30))..." -ForegroundColor Green

$login2 = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body '{"username":"testuser2","password":"Test123456"}' -ContentType "application/json"
$token2 = $login2.data.token
$h2 = @{ Authorization = "Bearer $token2" }
Write-Host "User2 logged in, token: $($token2.Substring(0,30))..." -ForegroundColor Green

# ---- 2. Rating Permission Tests ----
Write-Host "`n[2] Rating Permission Tests" -ForegroundColor Magenta

TestGet -Name "GET rating without login" -Url "$baseUrl/rating/1" -ExpectedCode "200" -Notes "Public read"

TestPost -Name "POST rating without login" -Url "$baseUrl/rating/1" -Body "score=5" -ExpectedCode "500" -Notes "Must login"

TestPost -Name "POST rating with login" -Url "$baseUrl/rating/1" -Headers $h1 -Body "score=5" -ExpectedCode "200" -Notes "Logged in rates"

$ratingWithUser = Invoke-RestMethod -Uri "$baseUrl/rating/1" -Method Get -Headers $h1
Write-Host "userScore=$($ratingWithUser.data.userScore)"
if ($ratingWithUser.data.userScore -eq 5) {
    Write-Host "[PASS] userScore returned" -ForegroundColor Green
    AddResult -tc "TC-$testNumber : GET rating returns userScore" -status "PASS" -expected "userScore=5" -actual "userScore=$($ratingWithUser.data.userScore)" -Notes "Returns userScore"
} else {
    Write-Host "[FAIL] userScore missing" -ForegroundColor Red
    AddResult -tc "TC-$testNumber : GET rating returns userScore" -status "FAIL" -expected "userScore=5" -actual "userScore=$($ratingWithUser.data.userScore)" -Notes "Returns userScore"
}

# ---- 3. Score Range Validation ----
Write-Host "`n[3] Score Range Validation" -ForegroundColor Magenta

TestPost -Name "Score=0 (invalid)" -Url "$baseUrl/rating/1" -Headers $h1 -Body "score=0" -ExpectedCode "500" -Notes "0 rejected"

TestPost -Name "Score=6 (invalid)" -Url "$baseUrl/rating/1" -Headers $h1 -Body "score=6" -ExpectedCode "500" -Notes "6 rejected"

TestPost -Name "Score=1 (boundary valid)" -Url "$baseUrl/rating/1" -Headers $h1 -Body "score=1" -ExpectedCode "200" -Notes "1 accepted"

TestPost -Name "Score=5 (boundary valid)" -Url "$baseUrl/rating/1" -Headers $h1 -Body "score=5" -ExpectedCode "200" -Notes "5 accepted"

# ---- 4. Recommendation Level Algorithm ----
Write-Host "`n[4] Recommendation Level Algorithm" -ForegroundColor Magenta

# Use scenic 77, 79, 82, 84 (all have 0 ratings)
# Level 0: avg < 3.5 -> scenic 77
Write-Host "`n--- Level 0 (avg < 3.5) - Scenic 77 ---" -ForegroundColor Yellow
TestPost -Name "User1 rates scenic 77 with 2" -Url "$baseUrl/rating/77" -Headers $h1 -Body "score=2" -ExpectedCode "200"
TestPost -Name "User2 rates scenic 77 with 3" -Url "$baseUrl/rating/77" -Headers $h2 -Body "score=3" -ExpectedCode "200"
$rating = Invoke-RestMethod -Uri "$baseUrl/rating/77" -Method Get
$scenic = Invoke-RestMethod -Uri "$baseUrl/scenic/77" -Method Get
$avg = [math]::Round($rating.data.averageScore, 2); $lvl = $scenic.data.recommendLevel
Write-Host "Result: avg=$avg level=$lvl"
if ($lvl -eq 0) {
    Write-Host "[PASS] Level 0" -ForegroundColor Green
    AddResult -tc "Level 0 (avg<3.5)" -status "PASS" -expected "Level 0" -actual "Level $lvl avg=$avg"
} else {
    Write-Host "[FAIL] Level 0" -ForegroundColor Red
    AddResult -tc "Level 0 (avg<3.5)" -status "FAIL" -expected "Level 0" -actual "Level $lvl avg=$avg"
}

# Level 1: 3.5 <= avg < 4.0 -> scenic 79
Write-Host "`n--- Level 1 (3.5 <= avg < 4.0) - Scenic 79 ---" -ForegroundColor Yellow
TestPost -Name "User1 rates scenic 79 with 4" -Url "$baseUrl/rating/79" -Headers $h1 -Body "score=4" -ExpectedCode "200"
TestPost -Name "User2 rates scenic 79 with 3" -Url "$baseUrl/rating/79" -Headers $h2 -Body "score=3" -ExpectedCode "200"
$rating = Invoke-RestMethod -Uri "$baseUrl/rating/79" -Method Get
$scenic = Invoke-RestMethod -Uri "$baseUrl/scenic/79" -Method Get
$avg = [math]::Round($rating.data.averageScore, 2); $lvl = $scenic.data.recommendLevel
Write-Host "Result: avg=$avg level=$lvl"
if ($lvl -eq 1) {
    Write-Host "[PASS] Level 1" -ForegroundColor Green
    AddResult -tc "Level 1 (3.5<=avg<4.0)" -status "PASS" -expected "Level 1" -actual "Level $lvl avg=$avg"
} else {
    Write-Host "[FAIL] Level 1" -ForegroundColor Red
    AddResult -tc "Level 1 (3.5<=avg<4.0)" -status "FAIL" -expected "Level 1" -actual "Level $lvl avg=$avg"
}

# Level 2: 4.0 <= avg < 4.5 -> scenic 82
Write-Host "`n--- Level 2 (4.0 <= avg < 4.5) - Scenic 82 ---" -ForegroundColor Yellow
TestPost -Name "User1 rates scenic 82 with 4" -Url "$baseUrl/rating/82" -Headers $h1 -Body "score=4" -ExpectedCode "200"
TestPost -Name "User2 rates scenic 82 with 4" -Url "$baseUrl/rating/82" -Headers $h2 -Body "score=4" -ExpectedCode "200"
$rating = Invoke-RestMethod -Uri "$baseUrl/rating/82" -Method Get
$scenic = Invoke-RestMethod -Uri "$baseUrl/scenic/82" -Method Get
$avg = [math]::Round($rating.data.averageScore, 2); $lvl = $scenic.data.recommendLevel
Write-Host "Result: avg=$avg level=$lvl"
if ($lvl -eq 2) {
    Write-Host "[PASS] Level 2" -ForegroundColor Green
    AddResult -tc "Level 2 (4.0<=avg<4.5)" -status "PASS" -expected "Level 2" -actual "Level $lvl avg=$avg"
} else {
    Write-Host "[FAIL] Level 2" -ForegroundColor Red
    AddResult -tc "Level 2 (4.0<=avg<4.5)" -status "FAIL" -expected "Level 2" -actual "Level $lvl avg=$avg"
}

# Level 3: avg >= 4.5 -> scenic 84
Write-Host "`n--- Level 3 (avg >= 4.5) - Scenic 84 ---" -ForegroundColor Yellow
TestPost -Name "User1 rates scenic 84 with 5" -Url "$baseUrl/rating/84" -Headers $h1 -Body "score=5" -ExpectedCode "200"
TestPost -Name "User2 rates scenic 84 with 5" -Url "$baseUrl/rating/84" -Headers $h2 -Body "score=5" -ExpectedCode "200"
$rating = Invoke-RestMethod -Uri "$baseUrl/rating/84" -Method Get
$scenic = Invoke-RestMethod -Uri "$baseUrl/scenic/84" -Method Get
$avg = [math]::Round($rating.data.averageScore, 2); $lvl = $scenic.data.recommendLevel
Write-Host "Result: avg=$avg level=$lvl"
if ($lvl -eq 3) {
    Write-Host "[PASS] Level 3" -ForegroundColor Green
    AddResult -tc "Level 3 (avg>=4.5)" -status "PASS" -expected "Level 3" -actual "Level $lvl avg=$avg"
} else {
    Write-Host "[FAIL] Level 3" -ForegroundColor Red
    AddResult -tc "Level 3 (avg>=4.5)" -status "FAIL" -expected "Level 3" -actual "Level $lvl avg=$avg"
}

# ---- 5. Modify Rating ----
Write-Host "`n[5] Modify Rating Test" -ForegroundColor Magenta

# Use a fresh scenic - check scenic 94
$r94 = Invoke-RestMethod -Uri "$baseUrl/rating/94" -Method Get
Write-Host "Scenic 94 ratingCount=$($r94.data.ratingCount)"

TestPost -Name "User1 rates scenic 94 with 3" -Url "$baseUrl/rating/94" -Headers $h1 -Body "score=3" -ExpectedCode "200"
$ratingBefore = Invoke-RestMethod -Uri "$baseUrl/rating/94" -Method Get
Write-Host "Before modify: avg=$($ratingBefore.data.averageScore)"

TestPost -Name "User1 modifies scenic 94 to 5" -Url "$baseUrl/rating/94" -Headers $h1 -Body "score=5" -ExpectedCode "200"
$ratingAfter = Invoke-RestMethod -Uri "$baseUrl/rating/94" -Method Get -Headers $h1
Write-Host "After modify: userScore=$($ratingAfter.data.userScore) avg=$($ratingAfter.data.averageScore)"
if ($ratingAfter.data.userScore -eq 5) {
    Write-Host "[PASS] Modify rating" -ForegroundColor Green
    AddResult -tc "Modify rating" -status "PASS" -expected "userScore=5" -actual "userScore=$($ratingAfter.data.userScore)"
} else {
    Write-Host "[FAIL] Modify rating" -ForegroundColor Red
    AddResult -tc "Modify rating" -status "FAIL" -expected "userScore=5" -actual "userScore=$($ratingAfter.data.userScore)"
}

# ---- 6. Collection Permission ----
Write-Host "`n[6] Collection Permission Tests" -ForegroundColor Magenta

# Test without login - should get 401
try {
    Invoke-RestMethod -Uri "$baseUrl/collection/scenic/1" -Method Post -Body "" -ContentType "application/x-www-form-urlencoded" | Out-Null
    Write-Host "[FAIL] Collection without login should fail" -ForegroundColor Red
    AddResult -tc "Collection without login" -status "FAIL" -expected "401/500" -actual "200" -Notes "Should require login"
} catch {
    Write-Host "[PASS] Collection without login rejected" -ForegroundColor Green
    AddResult -tc "Collection without login" -status "PASS" -expected "401/500" -actual "Rejected" -Notes "Requires login"
}

# Test with login (use scenic 69 which is less likely to be collected)
TestPost -Name "Collection with login" -Url "$baseUrl/collection/scenic/69" -Headers $h1 -Body "" -ExpectedCode "200" -Notes "Logged in collects"

# ---- 7. Frontend Test ----
Write-Host "`n[7] Frontend Service Test" -ForegroundColor Magenta

try {
    $fe = Invoke-WebRequest -Uri "http://localhost:5173/" -Method Get -UseBasicParsing
    if ($fe.StatusCode -eq 200) {
        Write-Host "[PASS] Frontend running" -ForegroundColor Green
        AddResult -tc "Frontend service" -status "PASS" -expected "Running" -actual "Status 200"
    } else {
        Write-Host "[FAIL] Frontend status" -ForegroundColor Red
        AddResult -tc "Frontend service" -status "FAIL" -expected "Running" -actual "Status $($fe.StatusCode)"
    }
} catch {
    Write-Host "[FAIL] Frontend unreachable" -ForegroundColor Red
    AddResult -tc "Frontend service" -status "FAIL" -expected "Running" -actual "Unreachable"
}

# ---- 8. Data Consistency ----
Write-Host "`n[8] Data Consistency Test" -ForegroundColor Magenta

$rating = Invoke-RestMethod -Uri "$baseUrl/rating/84" -Method Get
$scenic = Invoke-RestMethod -Uri "$baseUrl/scenic/84" -Method Get
if ([math]::Round($scenic.data.score, 2) -eq [math]::Round($rating.data.averageScore, 2)) {
    Write-Host "[PASS] Data consistent" -ForegroundColor Green
    AddResult -tc "Data consistency" -status "PASS" -expected "score=avgScore" -actual "score=$($scenic.data.score) avg=$($rating.data.averageScore)"
} else {
    Write-Host "[FAIL] Data inconsistent" -ForegroundColor Red
    AddResult -tc "Data consistency" -status "FAIL" -expected "score=avgScore" -actual "score=$($scenic.data.score) avg=$($rating.data.averageScore)"
}

# ---- Report ----
Write-Host "`n========================================" -ForegroundColor Yellow
Write-Host "TEST REPORT" -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Yellow

$passCount = ($testResults | Where-Object { $_.Status -eq "PASS" }).Count
$failCount = ($testResults | Where-Object { $_.Status -eq "FAIL" }).Count
$totalCount = $testResults.Count

Write-Host "Total: $totalCount" -ForegroundColor White
Write-Host "PASS: $passCount" -ForegroundColor Green
Write-Host "FAIL: $failCount" -ForegroundColor Red
if ($totalCount -gt 0) {
    Write-Host "Rate: $([math]::Round($passCount/$totalCount*100, 2))%" -ForegroundColor White
}

Write-Host "`nDetailed Results:" -ForegroundColor White
$testResults | Format-Table -AutoSize

$reportPath = "D:\bishe\test_report_$(Get-Date -Format 'yyyyMMdd_HHmmss').csv"
$testResults | Export-Csv -Path $reportPath -NoTypeInformation -Encoding UTF8
Write-Host "`nReport saved to: $reportPath" -ForegroundColor Green
