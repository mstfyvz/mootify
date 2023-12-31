package com.sebnem.mootify.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.raizlabs.android.dbflow.sql.language.SQLite
import com.sebnem.mootify.R
import com.sebnem.mootify.adapters.CheckListAdapter
import com.sebnem.mootify.databinding.ActivityCheckListBinding
import com.sebnem.mootify.db.CheckList
import com.sebnem.mootify.db.CheckList_Table
import com.sebnem.mootify.db.User
import com.sebnem.mootify.db.User_Table

class CheckListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckListBinding
    private lateinit var checkListAdapter: CheckListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkListAdapter = CheckListAdapter()
        binding.recyclerView.adapter = checkListAdapter
        val checkList = SQLite.select().from(CheckList::class.java).where(CheckList_Table.title.isNotNull).queryList()
        checkListAdapter.setData(checkList)

        binding.imageViewClose.setOnClickListener {
            onBackPressed()
        }
        binding.cardViewAddNew.setOnClickListener {
            binding.dialogNewCheck.editTextEnterNote.text.clear()
            if (binding.constraintlayoutCheckList.isVisible){
                binding.constraintlayoutCheckList.visibility = View.GONE
                binding.dialogNewCheck.root.visibility = View.VISIBLE
            }
        }

        binding.dialogNewCheck.buttonNo.setOnClickListener {
            if (binding.dialogNewCheck.root.isVisible){
                binding.constraintlayoutCheckList.visibility = View.VISIBLE
                binding.dialogNewCheck.root.visibility = View.GONE
            }
        }

        binding.dialogNewCheck.buttonYes.setOnClickListener {
            if (binding.dialogNewCheck.editTextEnterNote.text.toString().isEmpty()){
                Toast.makeText(this, "Lütfen boş alanları doldurunuz.", Toast.LENGTH_SHORT).show()
            } else {
                if (binding.dialogNewCheck.root.isVisible){
                    binding.constraintlayoutCheckList.visibility = View.VISIBLE
                    binding.dialogNewCheck.root.visibility = View.GONE
                }
                val item = CheckList(
                    title = binding.dialogNewCheck.editTextEnterNote.text.toString(),
                    isCompleted = false
                )
                item.save()
                val updatedCheckList = SQLite.select().from(CheckList::class.java).where(CheckList_Table.title.isNotNull).queryList()
                checkListAdapter.setData(updatedCheckList)
            }
        }
    }
}