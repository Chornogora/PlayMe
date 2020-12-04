$(document).ready(function() {
    showToasts();
    attachFiltersEventHandler();
    attachUserControlButtonsHandlers();
    attachSortingImagesHandlers();
    attachPaginationButtonsHandlers()
});

function showToasts(){
    const status = $('#show-toast-condition').data('operation-status');
    if(status == constants.STATUS.SUCCESSFUL){
        $('.successful-toast').toast('show');
    }
}

function attachFiltersEventHandler(){
    $("#apply-filters-button").on('click', ()=>{
        $("#offset-input").val(0);
        applyFilterParameters();
    });

    $("#clear-filters-button").on('click', ()=>{
        location.href = "/admin/users";
    });
}

function applyFilterParameters(){
    const parameters = {
        "login": $("#login-filter").val(),
        "email": $("#email-filter").val(),
        "firstName": $("#first-name-filter").val(),
        "lastName": $("#last-name-filter").val(),
        "birthdateFrom": $("#birthdate-filter-from").val(),
        "birthdateTo": $("#birthdate-filter-to").val(),
        "creationDateFrom": $("#creation-date-filter-from").val(),
        "creationDateTo": $("#creation-date-filter-to").val(),
        "roles": $("input[name='role-filter']:checked").map(function() {
            return $(this).val();
        }).get(),
        "statuses": $("input[name='status-filter']:checked").map(function() {
            return $(this).val();
        }).get(),
        "sortingField": $("[data-status]").data('field') || '',
        "sortingType": $("[data-status]").attr('data-status') || '',
        "limit": $("#limit-input").val(),
        "offset": $("#offset-input").val()
    }
    location.href = "/admin/users?"+ new URLSearchParams(parameters);
}

function attachUserControlButtonsHandlers(){
    $('.edit-user-button').on('click', (event)=>{
        const userId = $(event.target).data(constants.USER_ID_ATTRIBUTE_NAME);
        location.href = `/admin/users/edit/${userId}`;
    });

    $('#delete-user-modal').on('show.bs.modal', function (event) {
      const button = $(event.relatedTarget);
      const userId = $(button).data('user-id');
      const login = $(button).data('user-login');
      $("#modal-message-text").text(`Are you sure want to delete user ${login}?`);
      $("#modal-confirm-button").on('click', ()=>{
        const formData = new FormData();
        formData.append('userId', userId);
        fetch("/admin/users/delete/" + userId, {
            method: 'DELETE',
        }).then(()=>location.reload());
      });
    });
}

function attachSortingImagesHandlers(){
    $(".arrow-image").on('click', (event)=>{
        const image = event.target;
        const status = $(image).data("status");
        $("[data-status]").attr('data-status', null);
        $("[data-status]").data('status', null);
        switch(status){
            case 'ASC':
                $(image).attr('data-status', 'DESC');
                break;
            case 'DESC':
                $(image).attr('data-status', null);
                break;
            default:
                $(image).attr('data-status', 'ASC');
        }
        applyFilterParameters();
    });
}

function attachPaginationButtonsHandlers(){
    const limit = $("#limit-input").val();
    const offset = $("#offset-input").val();

    $("#pagination-left-button").on('click', (event)=>{
        $("#offset-input").val(offset-limit);
        applyFilterParameters();
    });

    $("#pagination-right-button").on('click', (event)=>{
        $("#offset-input").val(offset+limit);
        applyFilterParameters();
    });
}