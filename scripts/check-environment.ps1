# Check environment for building the Android project
# This script checks Java and Gradle versions and prints recommendations

Write-Host "Checking JDK and Gradle versions..."

$javaVersion = & java -version 2>&1
$gradleVersion = & ./gradlew -v 2>$null

Write-Host "Java version output:"
Write-Host $javaVersion

Write-Host "Gradle version output:"
Write-Host $gradleVersion

# Parse java version for convenience
$javaVersionLine = ($javaVersion -split "`n")[0]
if ($javaVersionLine -match 'version "([0-9]+)') {
  $major = [int]$Matches[1]
}
else {
  $major = 0
}

if ($major -ge 17 -and $major -lt 19) {
  Write-Host "OK: JDK $major detected — recommended for AGP/Kapt." -ForegroundColor Green
}
else {
  Write-Host "WARNING: JDK $major detected. For stable builds we recommend JDK 17 (OpenJDK 17)." -ForegroundColor Yellow
  Write-Host "Set JAVA_HOME or add org.gradle.java.home to gradle.properties if necessary."
}

Write-Host "Next steps:"
Write-Host "1) If you have JDK 17 installed, export it and re-run the build (set JAVA_HOME or org.gradle.java.home)."
Write-Host "2) Run: ./gradlew --stop; ./gradlew clean build --no-build-cache"

# Optionally offer to set org.gradle.java.home in gradle.properties if we find a JDK 17 path.
try {
  $javaExe = (Get-Command java -ErrorAction Stop).Source
  $javaHomeGuess = Split-Path -Parent $javaExe -Parent
  if ($javaHomeGuess -and (Test-Path "$javaHomeGuess/bin/java")) {
    $javaver = & "$javaHomeGuess/bin/java" -version 2>&1 | Out-String
    if ($javaver -match 'version "([0-9]+)') { $jMajor = [int]$Matches[1] } else { $jMajor = 0 }
    if ($jMajor -eq 17) {
      Write-Host "Detected JDK 17 installation at: $javaHomeGuess" -ForegroundColor Green
      $propFile = Join-Path $PWD 'gradle.properties'
      if (Test-Path $propFile) {
        $current = Get-Content $propFile -Raw
        if ($current -notmatch 'org.gradle.java.home') {
          $answer = Read-Host "Add org.gradle.java.home=$javaHomeGuess to gradle.properties now? (y/N)"
          if ($answer -match '^[yY]') {
            Add-Content $propFile "`norg.gradle.java.home=$javaHomeGuess"
            Write-Host "Added org.gradle.java.home=$javaHomeGuess to gradle.properties" -ForegroundColor Green
            Write-Host "Tip: Run './gradlew --stop' and re-run the build to use the new JDK toolchain." -ForegroundColor Green
          }
          else {
            Write-Host "Skipping edit of gradle.properties." -ForegroundColor Yellow
          }
        }
        else {
          Write-Host "gradle.properties already contains org.gradle.java.home; skipping." -ForegroundColor Yellow
        }
      }
    }
  }
}
catch {
  # ignore
}