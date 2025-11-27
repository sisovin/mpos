<#
Set the org.gradle.java.home in gradle.properties to the provided JDK path
Usage: .\scripts\set-java-home.ps1 -Path 'C:\Program Files\Java\jdk-17.0.8'
If no path is provided, the script attempts to detect java in PATH.
#>

param([string]$Path)

if (-not $Path) {
  try {
    $javaExe = (Get-Command java -ErrorAction Stop).Source
    $Path = Split-Path -Parent $javaExe -Parent
    Write-Host "Detected java at PATH: $Path"
  }
  catch {
    Write-Host "No java found in PATH. Please provide the JDK 17 path using -Path parameter." -ForegroundColor Red
    exit 1
  }
}

if (-not (Test-Path "$Path/bin/java")) {
  Write-Host "Provided path does not appear to contain a Java executable: $Path" -ForegroundColor Red
  exit 1
}

$javaver = & "$Path/bin/java" -version 2>&1 | Out-String
if ($javaver -notmatch 'version "([0-9]+)') {
  Write-Host "Unable to parse Java version for $Path" -ForegroundColor Red
  exit 1
}
$major = [int]$Matches[1]
if ($major -ne 17) {
  Write-Host "Detected Java major version: $major; recommended JDK 17 for stable builds." -ForegroundColor Yellow
  $ans = Read-Host "Continue to set org.gradle.java.home=$Path in gradle.properties? (y/N)"
  if ($ans -notmatch '^[yY]') { exit 0 }
}

$propFile = Join-Path $PWD 'gradle.properties'
if (-not (Test-Path $propFile)) {
  Write-Host "gradle.properties not found in repository root" -ForegroundColor Red
  exit 1
}

$current = Get-Content $propFile -Raw
if ($current -match 'org.gradle.java.home') {
  $ans = Read-Host "gradle.properties already has org.gradle.java.home entry. Overwrite? (y/N)"
  if ($ans -notmatch '^[yY]') { Write-Host "Aborting"; exit 0 }
  $new = $current -replace 'org.gradle.java.home=.*', "org.gradle.java.home=$Path"
  Set-Content $propFile $new -Force
  Write-Host "Updated org.gradle.java.home to: $Path" -ForegroundColor Green
}
else {
  Add-Content $propFile "`norg.gradle.java.home=$Path"
  Write-Host "Added org.gradle.java.home=$Path to gradle.properties" -ForegroundColor Green
}

Write-Host "Tip: Run './gradlew --stop' then './gradlew clean build --no-build-cache' to test." -ForegroundColor Green
