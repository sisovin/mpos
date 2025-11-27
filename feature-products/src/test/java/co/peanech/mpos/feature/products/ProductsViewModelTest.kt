package co.peanech.mpos.feature.products

import co.peanech.mpos.core.data.repository.FakeProductRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class ProductsViewModelTest {
    @Test
    fun `viewModel loads products`() = runTest {
        val repo = FakeProductRepository()
        val vm = ProductsViewModel(repo)
        val products = vm.products
        // Wait a small time for the flow to emit (Fake repo delays 200ms)
        kotlinx.coroutines.delay(300)
        assertThat(products.value).isNotEmpty()
    }
}
