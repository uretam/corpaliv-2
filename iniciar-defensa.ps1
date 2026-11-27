$proyecto = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $proyecto

if (-not (Test-Path 'tmp/db-env.ps1')) {
    throw 'Falta tmp/db-env.ps1. Esta carpeta privada contiene las credenciales locales.'
}
. .\tmp\db-env.ps1

$java = 'C:\Users\ASPIRINA\.jdks\jbr-21.0.11\bin\java.exe'
$mysql = Join-Path $proyecto 'tmp/mysql/mysql-8.4.13-winx64/bin/mysqld.exe'
$ini = (Join-Path $proyecto 'tmp/my.ini').Replace('\', '/')
$adb = Join-Path $env:LOCALAPPDATA 'Android\Sdk\platform-tools\adb.exe'
$emulador = Join-Path $env:LOCALAPPDATA 'Android\Sdk\emulator\emulator.exe'

if (-not (Get-NetTCPConnection -State Listen -LocalPort 3306 -ErrorAction SilentlyContinue)) {
    Start-Process $mysql -ArgumentList "--defaults-file=$ini" -WorkingDirectory $proyecto -WindowStyle Hidden
    Start-Sleep -Seconds 5
}
foreach ($servicio in @(
    @{ puerto = 8081; jar = 'backend/ms-contenidos/target/ms-contenidos-0.0.1-SNAPSHOT.jar' },
    @{ puerto = 8082; jar = 'backend/ms-vinculacion/target/ms-vinculacion-0.0.1-SNAPSHOT.jar' }
)) {
    if (-not (Get-NetTCPConnection -State Listen -LocalPort $servicio.puerto -ErrorAction SilentlyContinue)) {
        Start-Process $java -ArgumentList "-jar $($servicio.jar)" -WorkingDirectory $proyecto -WindowStyle Hidden
    }
}
Start-Sleep -Seconds 12
foreach ($puerto in 8081, 8082) {
    $estado = (Invoke-RestMethod "http://127.0.0.1:$puerto/actuator/health").status
    Write-Host "Servicio $puerto`: $estado"
}

if (-not ((& $adb devices) -match 'emulator-\d+\s+device')) {
    Start-Process $emulador -ArgumentList '-avd CORPALIV_Test_API_37 -no-audio -no-boot-anim -gpu swiftshader_indirect'
    & $adb wait-for-device
}
for ($intento = 0; $intento -lt 30; $intento++) {
    if ((& $adb shell getprop sys.boot_completed).Trim() -eq '1') { break }
    Start-Sleep -Seconds 3
}
$apk = if (Test-Path 'entregables/CORPALIV-2-firmado.apk') {
    'entregables/CORPALIV-2-firmado.apk'
} else {
    'android/app/build/outputs/apk/debug/app-debug.apk'
}
& $adb install -r $apk
& $adb shell am start -n 'cl.corpaliv.app/.MainActivity'
Write-Host 'CORPALIV abierto. El primer arranque del emulador puede tardar.'
