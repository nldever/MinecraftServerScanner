package managers

import models.view.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import scanServers

class ScanManager(private val vm: MainViewModel) {
    fun startScan(scope: CoroutineScope) {
        vm.scanning = true
        vm.stopScan = false
        vm.currentAction = "Сканируем..."
        vm.scanProgress = 0f
        vm.servers = listOf()

        scope.launch {
            val onlineServers = scanServers(
                ips = vm.targetIps,
                timeout = vm.currentTimeout,
                parallelLimit = vm.currentParallelLimit,
                stopCondition = { vm.stopScan },
                onProgress = { percent, text ->
                    vm.scanProgress = percent
                    vm.currentAction = text
                }
            )

            vm.servers = onlineServers
            vm.players = onlineServers.flatMap { it.players }.distinct()
            vm.currentAction = if (vm.stopScan)
                "Сканирование остановлено. Найдено: ${vm.servers.size} серверов."
            else
                "Сканирование завершено. Найдено: ${vm.servers.size} серверов."
            vm.scanning = false
            vm.stopScan = false
        }
    }

    fun stopScan() {
        vm.stopScan = true
        vm.currentAction = "Остановка..."
    }
}