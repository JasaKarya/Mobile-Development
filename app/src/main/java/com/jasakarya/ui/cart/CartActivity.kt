package com.jasakarya.ui.cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.jasakarya.databinding.ActivityCartBinding
import com.jasakarya.di.ViewModelFactory
import com.jasakarya.ui.detail.DetailActivity
import com.jasakarya.ui.payment.PaymentActivity
import com.jasakarya.ui.payment.TransactionsActivity

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: CartViewModel by viewModels {factory}
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        val userEmail = firebaseAuth.currentUser?.email.toString()
        viewModel.getCarts(userEmail)

        viewModel.carts.observe(this) { carts ->
            val cartAdapter = CartAdapter(onClick = { content ->

                val intent = Intent(this, PaymentActivity::class.java)
                val intContentId = content.contentId.toInt()
                intent.putExtra("contentId", intContentId)
                intent.putExtra("imgUrl", content.imgUrl)
                intent.putExtra("contentName", content.contentName)
                intent.putExtra("selectedPackage", content.selectedPackage.package_name)
                intent.putExtra("selectedPrice", content.selectedPackage.package_price)
                intent.putExtra("note", content.note)

                intent.putExtra("cartId", content.cartId)
                startActivity(intent)
            }
            , onTrash = {
                cart ->
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Hapus Item ini Dari Keranjang?")
                builder.setPositiveButton("Ya") { dialog, which ->
                    viewModel.deleteCart(cart.cartId)
                    viewModel.cartDeleteSuccess.observe(this) {
                        if (it) {
                            viewModel.getCarts(userEmail)
                            Toast.makeText(this, "Item Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Item Gagal Dihapus", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                builder.setNegativeButton("Tidak") { dialog, which ->
                    dialog.dismiss()
                    }
                builder.create().show()
            })
            binding.ibBack.setOnClickListener {
                finish()
            }

            binding.apply {
                with(rvCart) {
                    layoutManager = StaggeredGridLayoutManager(1, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
                    setHasFixedSize(true)
                    this.adapter = cartAdapter
                }
                cartAdapter.submitList(carts)
            }
        }

        binding.btnPesanan.setOnClickListener {
            val intent = Intent(this, TransactionsActivity::class.java)
            startActivity(intent)
        }

    }
}