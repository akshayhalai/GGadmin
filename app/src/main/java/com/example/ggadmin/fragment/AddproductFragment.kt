package com.example.ggadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.ggadmin.R
import com.example.ggadmin.adapter.AddProductImageAadapter
import com.example.ggadmin.databinding.FragmentAddproductBinding
import com.example.ggadmin.model.AddProductModel
import com.example.ggadmin.model.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class AddproductFragment : Fragment() {

private  lateinit var binding : FragmentAddproductBinding
private  lateinit var list : ArrayList<Uri>
private  lateinit var listImages : ArrayList<String>
private  lateinit var adapter : AddProductImageAadapter
private  lateinit var categoryList : ArrayList<String>
private  lateinit var dialog : Dialog
private var coverImage : Uri? = null
private var coverImageUrl : String? = ""

    private var lunchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK)
        {
            coverImage = it.data!!.data
            binding.productCoverImg.setImageURI(coverImage)
            binding.productCoverImg.visibility = VISIBLE
        }
    }

    private var lunchProductActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK)
        {
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddproductBinding.inflate(layoutInflater)
        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView((R.layout.progress_layout))
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener{
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            lunchGalleryActivity.launch(intent)
        }
        binding.productImgBtn.setOnClickListener{
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            lunchProductActivity.launch(intent)
        }
        setProductCategory()
        adapter = AddProductImageAadapter(list)
        binding.productImgRecycleView.adapter = adapter
        binding.submitProductBtn.setOnClickListener{
            validateData()
        }
        return binding.root
    }

    private fun validateData() {
        if(binding.productNameEdit.text.toString().isEmpty())
        {
            binding.productNameEdit.requestFocus()
            binding.productNameEdit.error = "Empty"
        }else if (binding.productSpEdit.text.toString().isEmpty())
        {
            binding.productSpEdit.requestFocus()
            binding.productSpEdit.error = "Empty"
        }else if (coverImage == null)
        {
            Toast.makeText(requireContext(), "Please Select Cover Image", Toast.LENGTH_SHORT).show()
        }else if (list.size < 1)
        {
            Toast.makeText(requireContext(), "Please select Product Image", Toast.LENGTH_SHORT).show()
        }else
        {
            upLoadImage()
        }
    }

    private fun upLoadImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    coverImageUrl = image.toString()

                    upLoadProductImage()
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong storage", Toast.LENGTH_SHORT).show()
            }
    }
    private var i = 0
    private fun upLoadProductImage() {
        dialog.show()
        val fileName = UUID.randomUUID().toString()+".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("products/$fileName")
        refStorage.putFile(list[i]!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    listImages.add(image!!.toString())
                    if(list.size == listImages.size)
                    {
                     storedata()
                    }else
                    {
                        i+=1
                        upLoadProductImage()
                    }
                }
            }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storedata() {

        val db = Firebase.firestore.collection("products")
        val key = db.document().id

        val data = AddProductModel(
            binding.productNameEdit.text.toString(),
            binding.productDescriptionEdit.text.toString(),
            coverImageUrl.toString(),
            categoryList[binding.productCategoryDropdown.selectedItemPosition],
            key,
            binding.productMrpEdit.text.toString(),
            binding.productSpEdit.text.toString(),
            listImages
        )

        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Product Added", Toast.LENGTH_SHORT).show()
            binding.productNameEdit.text = null
        }
            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setProductCategory()
    {
        categoryList = ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categoryList.clear()
            for(doc in it.documents)
            {
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.cate!!)
            }
            categoryList.add(0,"Select Category")

            val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item_layout,categoryList)
            binding.productCategoryDropdown.adapter = arrayAdapter
        }
    }
}